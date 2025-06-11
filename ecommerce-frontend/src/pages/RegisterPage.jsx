import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from '../api/axiosInstance';
import { motion } from 'framer-motion';
import Lottie from 'lottie-react';
import registerAnim from '../assets/register.json'; // ✅ Replace with your actual Lottie file path

const RegisterPage = () => {
  const [formData, setFormData] = useState({ name: '', email: '', password: '', role: '' });
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  const handleChange = e => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleRegister = async e => {
    e.preventDefault();
    try {
      const res = await axios.post('/auth/register', formData);
      setMessage('Registration successful! Redirecting to login...');
      setTimeout(() => navigate('/login'), 2000);
    } catch (error) {
      setMessage(error.response?.data?.message || 'Registration failed');
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
        <Lottie animationData={registerAnim} loop className="h-32 mx-auto mb-4" />
        <h2 className="text-4xl font-extrabold text-blue-700 mb-6 text-center">
          Sign <span className="text-yellow-400">Up</span>
        </h2>
        <form onSubmit={handleRegister} className="space-y-6">
          <input
            type="text"
            name="name"
            required
            placeholder="Full Name"
            onChange={handleChange}
            className="w-full p-3 rounded-lg border border-gray-300 focus:ring-2 focus:ring-yellow-400"
          />
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

          {/* Custom Role Selector */}
          <div>
            <label className="block text-gray-700 font-semibold mb-2">Select Role</label>
            <div className="flex justify-between gap-4">
              {['ADMIN', 'CUSTOMER'].map(role => (
                <label
                  key={role}
                  className={`flex-1 text-center py-3 rounded-lg cursor-pointer border transition 
                    ${
                      formData.role === role
                        ? 'bg-yellow-400 text-blue-900 font-bold border-yellow-500'
                        : 'bg-gray-100 text-gray-700 border-gray-300 hover:border-yellow-400'
                    }`}
                >
                  <input
                    type="radio"
                    name="role"
                    value={role}
                    onChange={handleChange}
                    className="hidden"
                    checked={formData.role === role}
                  />
                  {role === 'ADMIN' ? '👨‍💼 Admin' : '👤 Customer'}
                </label>
              ))}
            </div>
          </div>

          <button
            type="submit"
            className="w-full py-3 bg-yellow-400 text-blue-900 font-bold rounded-lg hover:bg-yellow-300 transition"
          >
            Register
          </button>

          {message && <p className="text-green-600 text-center">{message}</p>}

          <p className="text-center text-gray-600 mt-4">
            Already have an account?{' '}
            <Link to="/login" className="text-blue-600 font-semibold hover:underline">
              Login here
            </Link>
          </p>
        </form>
      </motion.div>
    </div>
  );
};

export default RegisterPage;
