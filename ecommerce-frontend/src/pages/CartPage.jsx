// src/pages/CartPage.jsx
import React, { useEffect, useState } from "react";
import axios from "../api/axiosInstance";
import Spinner from "../components/Spinner";
import { toast } from "react-toastify";
import emptyAnim from "../assets/empty.json";
import Lottie from "lottie-react";

const CartPage = () => {
  const [cartItems, setCartItems] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchCartItems = () => {
    const token = localStorage.getItem("token");
    axios
      .get("/cart/get/cart", {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((res) => setCartItems(res.data))
      .catch(() => toast.error("Failed to fetch cart"))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    fetchCartItems();
  }, []);

  const handleRemove = (productId) => {
    const token = localStorage.getItem("token");
    axios
      .delete(`/cart/remove/${productId}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then(() => fetchCartItems())
      .catch(() => toast.error("Remove failed"));
  };

  const handleQuantityChange = (productId, newQty) => {
    if (newQty < 1) return;
    const token = localStorage.getItem("token");
    axios
      .put(
        `/cart/update/cart`,
        { productId, quantity: newQty },
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      )
      .then(() => fetchCartItems())
      .catch(() => toast.error("Update failed"));
  };

  const handleClearCart = () => {
    const token = localStorage.getItem("token");
    axios
      .delete("/cart/clear", {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then(() => fetchCartItems())
      .catch(() => toast.error("Clear cart failed"));
  };

  const handlePlaceOrder = () => {
    const token = localStorage.getItem("token");
    axios
      .post("/order/place/fromCart", null, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then(() => {
        toast.success("✅ Order placed!");
        fetchCartItems();
      })
      .catch(() => toast.error("❌ Order failed"));
  };

  const total = cartItems.reduce(
    (acc, item) => acc + item.product.price * item.quantity,
    0
  );

  if (loading) return <Spinner />;

  return (
    <div className="p-4">
      <h2 className="text-xl font-semibold mb-4 dark:text-white">Your Cart</h2>

      {cartItems.length === 0 ? (
        <div className="flex flex-col items-center justify-center text-gray-600 dark:text-gray-300 mt-10">
          <Lottie animationData={emptyAnim} loop className="h-80" />
          <p className="text-lg mt-4">Your cart is empty</p>
        </div>
      ) : (
        <>
          {cartItems.map((item) => (
            <div key={item.id} className="border p-4 mb-2 dark:bg-gray-800 dark:text-white rounded">
              <h3 className="font-semibold">{item.product.name}</h3>
              <p>Price: ₹{item.product.price}</p>
              <div className="flex items-center gap-2 mt-1">
                <label>Quantity:</label>
                <input
                  type="number"
                  min={1}
                  value={item.quantity}
                  onChange={(e) =>
                    handleQuantityChange(item.product.id, parseInt(e.target.value))
                  }
                  className="border p-1 w-16 bg-white dark:text-black"
                />
                <button
                  onClick={() => handleRemove(item.product.id)}
                  className="bg-red-500 text-white px-3 py-1 rounded"
                >
                  Remove
                </button>
              </div>
            </div>
          ))}

          <hr className="my-4" />
          <h3 className="text-lg font-bold dark:text-white">Total: ₹{total}</h3>

          <div className="mt-4 flex gap-4">
            <button
              className="bg-red-600 text-white px-4 py-2 rounded"
              onClick={handleClearCart}
            >
              Clear Cart
            </button>
            <button
              className="bg-green-600 text-white px-4 py-2 rounded"
              onClick={handlePlaceOrder}
            >
              ✅ Place Order
            </button>
          </div>
        </>
      )}
    </div>
  );
};

export default CartPage;
