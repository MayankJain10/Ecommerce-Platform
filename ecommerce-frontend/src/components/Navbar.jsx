// src/components/Navbar.jsx
import React from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';

const Navbar = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const token = localStorage.getItem('token');
  const isLoggedIn = !!token;
  const isAuthPage = location.pathname === '/login' || location.pathname === '/register';

  // 🔐 Safe decoding of role from JWT token
  let role = null;
  if (token) {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      role = payload.role;
    } catch (e) {
      console.warn("Invalid token");
    }
  }

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

  return (
    <nav className="bg-white shadow-md px-8 py-4 flex justify-between items-center sticky top-0 z-50">
      <div className="flex items-center gap-2">
        <span className="text-2xl">☸️</span>
        <Link to="/" className="text-2xl font-extrabold text-blue-700 tracking-wide hover:text-yellow-500 transition">
          Ecommerce Platform
        </Link>
      </div>

      {isLoggedIn && !isAuthPage && (
        <div className="flex items-center space-x-6">
          <Link to="/products" className="hover:text-blue-600 transition font-semibold">
            🛒 Products
          </Link>
          <Link to="/cart" className="hover:text-blue-600 transition font-semibold">
            🧺 Cart
          </Link>
          <Link to="/orders" className="hover:text-blue-600 transition font-semibold">
            📦 Orders
          </Link>
          {role === 'ADMIN' && (
            <Link to="/admin/dashboard" className="hover:text-blue-600 transition font-semibold">
              ⚙️ Admin
            </Link>
          )}
          <button
            onClick={handleLogout}
            className="bg-red-500 hover:bg-red-600 text-white px-4 py-1.5 rounded transition"
          >
            Logout
          </button>
          <button
  onClick={() => {
    document.documentElement.classList.toggle("dark");
  }}
  className="ml-4 text-sm bg-gray-800 text-white px-3 py-1 rounded"
>
  🌗 Toggle Dark Mode
</button>

        </div>
      )}
    </nav>
  );
};

export default Navbar;
