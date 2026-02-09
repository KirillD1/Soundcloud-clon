import React from 'react';
import { FaPlay, FaPause, FaHeart } from 'react-icons/fa';
import { likesAPI } from '../services/api';

const TrackCard = ({ track, onPlay, isPlaying }) => {
  const [isLiked, setIsLiked] = React.useState(track.isLiked);
  const [likesCount, setLikesCount] = React.useState(track.likesCount);

  const handleLike = async (e) => {
    e.stopPropagation();
    try {
      await likesAPI.toggle(track.id);
      setIsLiked(!isLiked);
      setLikesCount(isLiked ? likesCount - 1 : likesCount + 1);
    } catch (error) {
      console.error('Ошибка лайка:', error);
    }
  };

  return (
    <div
      onClick={() => onPlay(track)}
      className="bg-gray-800 p-4 rounded-lg hover:bg-gray-750 cursor-pointer transition flex items-center gap-4"
    >
      <button
        className="w-12 h-12 rounded-full bg-orange-500 flex items-center justify-center hover:bg-orange-600 transition flex-shrink-0"
        onClick={(e) => {
          e.stopPropagation();
          onPlay(track);
        }}
      >
        {isPlaying ? <FaPause className="text-white" /> : <FaPlay className="text-white ml-1" />}
      </button>

      {track.coverUrl && (
        <img
          src={track.coverUrl}
          alt={track.title}
          className="w-16 h-16 rounded object-cover"
        />
      )}

      <div className="flex-1 min-w-0">
        <h3 className="text-white font-semibold truncate">{track.title}</h3>
        <p className="text-gray-400 text-sm truncate">{track.user.displayName}</p>
        {track.genre && (
          <span className="inline-block text-xs text-gray-500 mt-1">#{track.genre}</span>
        )}
      </div>

      <div className="flex items-center gap-4 text-gray-400">
        <button
          onClick={handleLike}
          className={`flex items-center gap-1 hover:text-red-500 transition ${
            isLiked ? 'text-red-500' : ''
          }`}
        >
          <FaHeart />
          <span className="text-sm">{likesCount}</span>
        </button>
        
        <span className="text-sm">{track.playsCount} прослушиваний</span>
      </div>
    </div>
  );
};

export default TrackCard;
