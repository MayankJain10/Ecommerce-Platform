// src/components/ProductCard.jsx
import React from "react";
import axios from "axios";
import { toast } from "react-toastify";

const ProductCard = ({ product }) => {
  const handleAddToCart = (productId) => {
    const token = localStorage.getItem("token");
    axios
      .post(
        "http://localhost:8081/api/cart/addTo/cart",
        { productId, quantity: 1 },
        { headers: { Authorization: `Bearer ${token}` } }
      )
      .then(() => toast.success("✅ Added to cart"))
      .catch(() => toast.error("❌ Failed to add to cart"));
  };

  return (
    <div className="p-4 border rounded shadow bg-white dark:bg-gray-800 dark:text-white">
      <img
        src={product.imageUrl || "https://via.placeholder.com/150"}
        alt={product.name}
        className="w-full h-48 object-cover rounded mb-2"
      />
      <h2 className="text-xl font-semibold">{product.name}</h2>
      <p>{product.description}</p>
      <p className="font-bold text-lg">₹{product.price}</p>
      <button
        className="mt-2 bg-blue-500 hover:bg-blue-600 text-white px-3 py-1 rounded"
        onClick={() => handleAddToCart(product.id)}
      >
        Add to Cart
      </button>
    </div>
  );
};

export default ProductCard;
