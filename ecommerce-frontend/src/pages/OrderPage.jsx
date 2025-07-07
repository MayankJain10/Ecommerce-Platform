// src/pages/OrderPage.jsx
import React, { useEffect, useState } from "react";
import axios from "../api/axiosInstance";
import Spinner from "../components/Spinner";
import { toast } from "react-toastify";
import Lottie from "lottie-react";
import emptyAnim from "../assets/empty.json";

const OrderPage = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchOrders = () => {
    const token = localStorage.getItem("token");
    axios
      .get("/order/getMy/order", {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((res) => setOrders(res.data))
      .catch(() => toast.error("❌ Failed to fetch orders"))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    fetchOrders();
  }, []);

  const handleCancel = (orderId) => {
    const token = localStorage.getItem("token");
    axios
      .delete(`/order/cancel/${orderId}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then(() => {
        toast.success("✅ Order cancelled");
        fetchOrders();
      })
      .catch(() => toast.error("❌ Cancel failed"));
  };

  const handleDownloadInvoice = (orderId) => {
    const token = localStorage.getItem("token");
    axios
      .get(`/invoice/${orderId}`, {
        headers: { Authorization: `Bearer ${token}` },
        responseType: "blob",
      })
      .then((res) => {
        const url = window.URL.createObjectURL(new Blob([res.data]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", `invoice_${orderId}.pdf`);
        document.body.appendChild(link);
        link.click();
        toast.success("✅ Invoice downloaded");
      })
      .catch(() => toast.error("❌ Unable to download invoice"));
  };

  if (loading) return <Spinner />;

  return (
    <div className="p-6">
      <h2 className="text-xl font-bold mb-4">📦 Your Orders</h2>

      {orders.length === 0 ? (
        <div className="text-center mt-10">
          <Lottie animationData={emptyAnim} loop className="h-72 mx-auto" />
          <p className="text-gray-600 mt-4 text-lg">No orders found. Start shopping now!</p>
        </div>
      ) : (
        orders.map((order) => (
          <div key={order.id} className="border p-4 mb-4 shadow-md rounded bg-white dark:bg-gray-800">
            <p><strong>🆔 Order ID:</strong> {order.id}</p>
            <p><strong>🕒 Date:</strong> {new Date(order.orderDate).toLocaleString()}</p>
            <p><strong>💰 Total:</strong> ₹{order.totalAmount}</p>
            <h4 className="mt-2 font-semibold">🛍️ Items:</h4>
            <ul className="list-disc ml-6 text-gray-800 dark:text-gray-100">
              {order.items.map((item) => (
                <li key={item.id}>
                  {item.product.name} – {item.quantity} x ₹{item.price}
                </li>
              ))}
            </ul>

            {!order.cancelled && (
              <div className="mt-4 space-x-4">
                <button
                  onClick={() => handleCancel(order.id)}
                  className="bg-red-500 hover:bg-red-600 text-white px-4 py-1 rounded"
                >
                  Cancel Order
                </button>
                <button
                  onClick={() => handleDownloadInvoice(order.id)}
                  className="bg-green-600 hover:bg-green-700 text-white px-4 py-1 rounded"
                >
                  Download Invoice
                </button>
              </div>
            )}
          </div>
        ))
      )}
    </div>
  );
};

export default OrderPage;
