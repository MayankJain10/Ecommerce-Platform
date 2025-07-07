import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from '../api/axiosInstance';
import { motion } from 'framer-motion';
import Lottie from 'lottie-react';
import loginAnim from '../assets/login.json'; // ✅ Replace with your actual Lottie file path
import { toast } from 'react-toastify';

const LoginPage = () => {
  const [formData, setFormData] = useState({ email: '', password: '' });
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  const handleChange = e => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleLogin = async e => {
    e.preventDefault();
    try {
      const res = await axios.post('/auth/login', formData);
      localStorage.setItem('token', res.data);
      setMessage('');
      navigate('/products');
    } catch (error) {
      toast.error(error.response?.data?.message || 'Login failed');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-r from-blue-300 via-indigo-400 to-blue-700 px-4">
      <motion.div
        initial={{ opacity: 0, y: 30 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6 }}
        className="bg-white rounded-2xl shadow-2xl p-10 max-w-md w-full"
      >
        <Lottie animationData={loginAnim} loop className="h-32 mx-auto mb-4" />
        <h2 className="text-4xl font-extrabold text-blue-700 mb-6 text-center">
          Sign <span className="text-yellow-400">In</span>
        </h2>
        <form onSubmit={handleLogin} className="space-y-6">
          <input
            type="email"
            name="email"
            required
            placeholder="Email"
            onChange={handleChange}
            className="w-full p-3 rounded-lg border border-gray-300 focus:ring-2 focus:ring-yellow-400"
          />
          <input
            type="password"
            name="password"
            required
            placeholder="Password"
            onChange={handleChange}
            className="w-full p-3 rounded-lg border border-gray-300 focus:ring-2 focus:ring-yellow-400"
          />

          <button
            type="submit"
            className="w-full py-3 bg-yellow-400 text-blue-900 font-bold rounded-lg hover:bg-yellow-300 transition"
          >
            Login
          </button>

          {message && <p className="text-red-500 text-center">{message}</p>}

          <p className="text-center text-gray-600 mt-4">
            New user?{' '}
            <Link to="/register" className="text-blue-600 font-semibold hover:underline">
              Register here
            </Link>
          </p>
        </form>
      </motion.div>
    </div>
  );
};

export default LoginPage;
