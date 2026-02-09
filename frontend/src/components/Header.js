import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { FaMusic, FaUpload, FaUser, FaSignOutAlt } from 'react-icons/fa';

const Header = () => {
  const { user, logout } = useAuth();

  return (
    <header className="bg-gray-800 border-b border-gray-700">
      <div className="max-w-6xl mx-auto px-4">
        <div className="flex items-center justify-between h-16">
          <Link to="/" className="flex items-center gap-2 text-orange-500 text-2xl font-bold">
            <FaMusic />
            <span>SoundCloud Clone</span>
          </Link>

          <nav className="flex items-center gap-6">
            {user ? (
              <>
                <Link
                  to="/upload"
                  className="flex items-center gap-2 text-white hover:text-orange-500 transition"
                >
                  <FaUpload />
                  <span>Загрузить</span>
                </Link>
                
                <div className="flex items-center gap-4">
                  <Link
                    to="/profile"
                    className="flex items-center gap-2 text-white hover:text-orange-500 transition"
                  >
                    <FaUser />
                    <span>{user.displayName}</span>
                  </Link>
                  
                  <button
                    onClick={logout}
                    className="text-gray-400 hover:text-white transition"
                  >
                    <FaSignOutAlt />
                  </button>
                </div>
              </>
            ) : (
              <>
                <Link
                  to="/login"
                  className="text-white hover:text-orange-500 transition"
                >
                  Войти
                </Link>
                <Link
                  to="/register"
                  className="bg-orange-500 text-white px-4 py-2 rounded hover:bg-orange-600 transition"
                >
                  Регистрация
                </Link>
              </>
            )}
          </nav>
        </div>
      </div>
    </header>
  );
};

export default Header;
