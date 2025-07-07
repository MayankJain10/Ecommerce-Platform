package com.mayank.ecommerce.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.mayank.ecommerce.config.JwtUtil;
import com.mayank.ecommerce.entity.Order;
import com.mayank.ecommerce.entity.OrderItem;
import com.mayank.ecommerce.entity.User;
import com.mayank.ecommerce.repository.OrderRepository;
import com.mayank.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class InvoiceController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/invoice/{orderId}")
    public ResponseEntity<byte[]> getInvoice(@PathVariable Long orderId, @RequestHeader("Authorization") String authHeader) throws IOException, DocumentException {
        String email = jwtUtil.extractUsername(authHeader.substring(7));
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("Unauthorized");
        }

        if (order.isCancelled()) {
            throw new RuntimeException("Cannot generate invoice for a cancelled order");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();

        document.add(new Paragraph("Invoice for Order #" + order.getId()));
        document.add(new Paragraph("Customer: " + order.getUser().getName()));
        document.add(new Paragraph("Email: " + order.getUser().getEmail()));
        document.add(new Paragraph("Order Date: " + order.getOrderDate()));
        document.add(new Paragraph("Total Amount: ₹" + order.getTotalAmount()));
        document.add(new Paragraph("Items:"));

        for (OrderItem item : order.getItems()) {
            String itemLine = item.getProduct().getName() + " - " + item.getQuantity() + " x ₹" + item.getPrice();
            document.add(new Paragraph(itemLine));
        }

        document.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "invoice_" + orderId + ".pdf");

        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
    }

}
