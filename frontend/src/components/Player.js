import React, { useEffect, useRef, useState } from 'react';
import WaveSurfer from 'wavesurfer.js';
import { FaPlay, FaPause, FaVolumeUp } from 'react-icons/fa';
import { tracksAPI } from '../services/api';

const Player = ({ track }) => {
  const waveformRef = useRef(null);
  const wavesurfer = useRef(null);
  const [isPlaying, setIsPlaying] = useState(false);
  const [volume, setVolume] = useState(0.5);

  useEffect(() => {
    if (waveformRef.current) {
      wavesurfer.current = WaveSurfer.create({
        container: waveformRef.current,
        waveColor: '#555',
        progressColor: '#ff5500',
        cursorColor: '#ff5500',
        barWidth: 2,
        barRadius: 3,
        height: 60,
        responsive: true,
      });

      wavesurfer.current.on('ready', () => {
        wavesurfer.current.setVolume(volume);
      });

      wavesurfer.current.on('play', () => setIsPlaying(true));
      wavesurfer.current.on('pause', () => setIsPlaying(false));
    }

    return () => {
      if (wavesurfer.current) {
        wavesurfer.current.destroy();
      }
    };
  }, []);

  useEffect(() => {
    if (wavesurfer.current && track) {
      const audioUrl = tracksAPI.stream(track.id);
      wavesurfer.current.load(audioUrl);
    }
  }, [track]);

  useEffect(() => {
    if (wavesurfer.current) {
      wavesurfer.current.setVolume(volume);
    }
  }, [volume]);

  const togglePlayPause = () => {
    if (wavesurfer.current) {
      wavesurfer.current.playPause();
    }
  };

  if (!track) return null;

  return (
    <div className="fixed bottom-0 left-0 right-0 bg-gray-800 border-t border-gray-700 p-4 z-50">
      <div className="max-w-6xl mx-auto">
        <div className="flex items-center gap-4 mb-2">
          <button
            onClick={togglePlayPause}
            className="w-10 h-10 rounded-full bg-orange-500 flex items-center justify-center hover:bg-orange-600"
          >
            {isPlaying ? <FaPause className="text-white" /> : <FaPlay className="text-white ml-1" />}
          </button>

          <div className="flex-1 min-w-0">
            <h4 className="text-white font-semibold truncate">{track.title}</h4>
            <p className="text-gray-400 text-sm truncate">{track.user.displayName}</p>
          </div>

          <div className="flex items-center gap-2">
            <FaVolumeUp className="text-gray-400" />
            <input
              type="range"
              min="0"
              max="1"
              step="0.01"
              value={volume}
              onChange={(e) => setVolume(parseFloat(e.target.value))}
              className="w-24"
            />
          </div>
        </div>

        <div ref={waveformRef} className="w-full" />
      </div>
    </div>
  );
};

export default Player;
