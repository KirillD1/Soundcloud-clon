import React, { useState, useEffect } from 'react';
import { tracksAPI } from '../services/api';
import TrackCard from '../components/TrackCard';
import Player from '../components/Player';

const Home = () => {
  const [tracks, setTracks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [currentTrack, setCurrentTrack] = useState(null);

  useEffect(() => {
    loadTracks();
  }, []);

  const loadTracks = async () => {
    try {
      const response = await tracksAPI.getAll({ page: 0, size: 20 });
      setTracks(response.data.content);
    } catch (error) {
      console.error('Ошибка загрузки треков:', error);
    } finally {
      setLoading(false);
    }
  };

  const handlePlay = (track) => {
    setCurrentTrack(track);
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen bg-gray-900">
        <div className="text-white text-xl">Загрузка...</div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-900 pb-32">
      <div className="max-w-6xl mx-auto px-4 py-8">
        <h1 className="text-4xl font-bold text-white mb-8">Недавние треки</h1>
        
        <div className="space-y-4">
          {tracks.map((track) => (
            <TrackCard
              key={track.id}
              track={track}
              onPlay={handlePlay}
              isPlaying={currentTrack?.id === track.id}
            />
          ))}
        </div>

        {tracks.length === 0 && (
          <div className="text-center text-gray-400 py-12">
            Нет доступных треков
          </div>
        )}
      </div>

      {currentTrack && <Player track={currentTrack} />}
    </div>
  );
};

export default Home;
