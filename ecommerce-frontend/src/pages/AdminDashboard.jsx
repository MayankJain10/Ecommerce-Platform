// src/pages/AdminDashboard.jsx
import React, { useEffect, useState } from "react";
import axios from "../api/axiosInstance";
import { toast } from "react-toastify";
import { motion } from "framer-motion";

const AdminDashboard = () => {
  const [analytics, setAnalytics] = useState({});
  const [users, setUsers] = useState([]);
  const [products, setProducts] = useState([]);

  const token = localStorage.getItem("token");
  const headers = { Authorization: `Bearer ${token}` };

  useEffect(() => {
    axios.get("/admin/analytics", { headers })
      .then(res => setAnalytics(res.data))
      .catch(() => toast.error("❌ Failed to fetch analytics"));

    axios.get("/admin/users", { headers })
      .then(res => setUsers(res.data))
      .catch(() => toast.error("❌ Failed to fetch users"));

    axios.get("/products/allproducts", { headers })
      .then(res => setProducts(res.data))
      .catch(() => toast.error("❌ Failed to fetch products"));
  }, []);

  const handleToggle = (id) => {
    axios.put(`/products/toggle-product/${id}`, null, { headers })
      .then(() => {
        setProducts(prev => prev.map(p => p.id === id ? { ...p, active: !p.active } : p));
        toast.success("✅ Product status updated");
      })
      .catch(() => toast.error("❌ Toggle failed"));
  };

  const handleStockUpdate = (id, quantity) => {
    axios.put(`/admin/product/stock/${id}?quantity=${quantity}`, {}, { headers })
      .then(() => toast.success("✅ Stock updated"))
      .catch(() => toast.error("❌ Stock update failed"));
  };

  const handleDeactivateUser = (email) => {
    axios.put(`/admin/deactivateUser?email=${email}`, {}, { headers })
      .then(() => {
        setUsers(prev => prev.map(u => u.email === email ? { ...u, active: false } : u));
        toast.success("✅ User deactivated");
      })
      .catch(() => toast.error("❌ User deactivation failed"));
  };

  const handleImageUrlUpdate = (id, imageUrl) => {
    axios.put(`/admin/product/image/${id}`, { imageUrl }, { headers })
      .then(() => toast.success("✅ Image URL updated"))
      .catch(() => toast.error("❌ Failed to update image"));
  };

  return (
    <div className="p-6 space-y-6">
      <motion.h1
        className="text-3xl font-bold text-blue-800"
        initial={{ opacity: 0, y: -30 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6 }}
      >
        📊 Admin Dashboard
      </motion.h1>

      <div className="grid grid-cols-3 gap-4">
        <div className="bg-blue-100 p-6 rounded-xl shadow">
          <h2 className="text-xl font-semibold">👤 Users</h2>
          <p className="text-3xl">{analytics.totalUsers}</p>
        </div>
        <div className="bg-green-100 p-6 rounded-xl shadow">
          <h2 className="text-xl font-semibold">📦 Orders</h2>
          <p className="text-3xl">{analytics.totalOrders}</p>
        </div>
        <div className="bg-yellow-100 p-6 rounded-xl shadow">
          <h2 className="text-xl font-semibold">💰 Revenue</h2>
          <p className="text-3xl">₹{analytics.totalRevenue}</p>
        </div>
      </div>

      <hr className="my-6" />

      <h2 className="text-2xl font-semibold text-blue-700 mt-4">📋 All Users</h2>
      <div className="overflow-x-auto">
        <table className="min-w-full mt-2 bg-white rounded shadow">
          <thead className="bg-gray-200 text-left">
            <tr>
              <th className="p-3">Name</th>
              <th>Email</th>
              <th>Role</th>
              <th>Status</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {users.map(u => (
              <tr key={u.id} className="border-t">
                <td className="p-3">{u.name}</td>
                <td>{u.email}</td>
                <td>{u.role}</td>
                <td>{u.active ? "✅ Active" : "❌ Deactivated"}</td>
                <td>
                  {u.active && (
                    <button
                      onClick={() => handleDeactivateUser(u.email)}
                      className="bg-red-500 hover:bg-red-600 text-white px-3 py-1 rounded"
                    >
                      Deactivate
                    </button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <h2 className="text-2xl font-semibold text-blue-700 mt-6">📋 Products</h2>
      <div className="overflow-x-auto">
        <table className="min-w-full mt-2 bg-white rounded shadow">
          <thead className="bg-gray-200 text-left">
            <tr>
              <th className="p-3">Name</th>
              <th>Price</th>
              <th>Stock</th>
              <th>Image URL</th>
              <th>Active</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {products.map(p => (
              <tr key={p.id} className="border-t">
                <td className="p-3">{p.name}</td>
                <td>₹{p.price}</td>
                <td>
                  <input
                    type="number"
                    defaultValue={p.quantity}
                    min={0}
                    onBlur={(e) => handleStockUpdate(p.id, e.target.value)}
                    className="border rounded p-1 w-20"
                  />
                </td>
                <td>
                  <input
                    type="text"
                    defaultValue={p.imageUrl || ""}
                    onBlur={(e) => handleImageUrlUpdate(p.id, e.target.value)}
                    className="border p-1 w-full"
                    placeholder="https://..."
                  />
                </td>
                <td>{p.active ? "✅" : "❌"}</td>
                <td>
                  <button
                    onClick={() => handleToggle(p.id)}
                    className="bg-blue-500 hover:bg-blue-600 text-white px-3 py-1 rounded"
                  >
                    Toggle
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default AdminDashboard;
