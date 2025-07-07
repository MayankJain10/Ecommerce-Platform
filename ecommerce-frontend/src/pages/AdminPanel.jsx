import React, { useEffect, useState } from "react";
import axios from "../api/axiosInstance";

const AdminPanel = () => {
  const token = localStorage.getItem("token");

  const [products, setProducts] = useState([]);
  const [users, setUsers] = useState([]);
  const [stats, setStats] = useState(null);

  // Fetch products, users, and analytics
  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const productRes = await axios.get("/admin/products", {
        headers: { Authorization: `Bearer ${token}` },
      });
      setProducts(productRes.data);

      const userRes = await axios.get("/admin/users", {
        headers: { Authorization: `Bearer ${token}` },
      });
      setUsers(userRes.data);

      const analyticsRes = await axios.get("/admin/analytics", {
        headers: { Authorization: `Bearer ${token}` },
      });
      setStats(analyticsRes.data);
    } catch (err) {
      console.error("Error fetching admin data:", err);
    }
  };

  const toggleProductAvailability = async (productId) => {
    try {
      await axios.put(`/admin/product/toggle/${productId}`, {}, {
        headers: { Authorization: `Bearer ${token}` },
      });
      fetchData();
    } catch (err) {
      console.error("Toggle product failed:", err);
    }
  };

  const updateStock = async (productId, newQty) => {
    if (newQty < 0) return;
    try {
      await axios.put(
        `/admin/product/stock/${productId}`,
        { quantity: newQty },
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      fetchData();
    } catch (err) {
      console.error("Stock update failed:", err);
    }
  };

  const deactivateUser = async (email) => {
    try {
      await axios.put(`/admin/user/deactivate/${email}`, {}, {
        headers: { Authorization: `Bearer ${token}` },
      });
      fetchData();
    } catch (err) {
      console.error("Deactivate failed:", err);
    }
  };

  return (
    <div className="p-6">
      <h1 className="text-3xl font-bold mb-6 text-blue-800">🛠️ Admin Panel</h1>

      {/* Analytics */}
      {stats && (
        <div className="grid grid-cols-3 gap-6 mb-8">
          <div className="bg-yellow-100 p-4 rounded-lg shadow">
            <h3 className="text-xl font-semibold text-yellow-800">Users</h3>
            <p className="text-2xl">{stats.totalUsers}</p>
          </div>
          <div className="bg-green-100 p-4 rounded-lg shadow">
            <h3 className="text-xl font-semibold text-green-800">Orders</h3>
            <p className="text-2xl">{stats.totalOrders}</p>
          </div>
          <div className="bg-indigo-100 p-4 rounded-lg shadow">
            <h3 className="text-xl font-semibold text-indigo-800">Revenue</h3>
            <p className="text-2xl">₹{stats.totalRevenue}</p>
          </div>
        </div>
      )}

      {/* Products */}
      <div className="mb-10">
        <h2 className="text-2xl font-bold mb-3">🛒 Products</h2>
        {products.map((p) => (
          <div key={p.id} className="border p-4 rounded mb-4 bg-white">
            <h3 className="font-semibold">{p.name}</h3>
            <p>{p.description}</p>
            <p>Price: ₹{p.price}</p>
            <div className="flex gap-4 mt-2 items-center">
              <label>Stock:</label>
              <input
                type="number"
                min={0}
                defaultValue={p.quantity}
                onBlur={(e) => updateStock(p.id, parseInt(e.target.value))}
                className="border px-2 py-1 w-24"
              />
              <button
                onClick={() => toggleProductAvailability(p.id)}
                className={`px-3 py-1 rounded text-white ${
                  p.available ? "bg-green-600" : "bg-gray-600"
                }`}
              >
                {p.available ? "Active" : "Inactive"}
              </button>
            </div>
          </div>
        ))}
      </div>

      {/* Users */}
      <div>
        <h2 className="text-2xl font-bold mb-3">👤 Users</h2>
        {users.map((u) => (
          <div
            key={u.id}
            className="border p-4 mb-3 rounded bg-white flex justify-between items-center"
          >
            <div>
              <h4 className="font-semibold">
                {u.name} ({u.email})
              </h4>
              <p>Role: {u.role}</p>
            </div>
            <button
              className={`px-4 py-1 rounded text-white ${
                u.active ? "bg-red-500" : "bg-gray-400"
              }`}
              onClick={() => deactivateUser(u.email)}
            >
              {u.active ? "Deactivate" : "Inactive"}
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default AdminPanel;
