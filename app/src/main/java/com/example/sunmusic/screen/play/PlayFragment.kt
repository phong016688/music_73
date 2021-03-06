package com.example.sunmusic.screen.play

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.sunmusic.R
import com.example.sunmusic.screen.main.MainActivity
import com.example.sunmusic.utils.loadFromUrl
import com.example.sunmusic.utils.musicplayer.MusicMediaPlayer
import com.example.sunmusic.utils.musicplayer.StateMusic
import com.example.sunmusic.utils.toast
import kotlinx.android.synthetic.main.fragment_play.view.*
import kotlinx.android.synthetic.main.layout_bind_music.view.*

class PlayFragment : Fragment(R.layout.fragment_play), MusicMediaPlayer.MediaListener {
    private var musicPlayer: MusicMediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        musicPlayer = (activity as? MainActivity)?.getMusicService()?.musicPlayer
        musicPlayer?.startMusic()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_alpha)
        view.imageMusicLayout.startAnimation(animation)
        view.updatePlayMusic()
        musicPlayer?.addOnStateChangeListener(this)
    }

    override fun onDestroyView() {
        musicPlayer?.removeOnStateChangeListener(this)
        super.onDestroyView()
    }

    override fun onDestroy() {
        musicPlayer = null
        super.onDestroy()
    }

    override fun onStateChange(state: StateMusic) {
        view?.apply {
            when (state) {
                StateMusic.STARTED -> updatePlayMusic()
                StateMusic.PAUSED -> binMusicLayout?.playImageView?.setBackgroudPauseMusic()
                StateMusic.PREPARING -> showNextMusic()
                else -> Unit
            }
        }
    }

    private fun showNextMusic() {
        context?.toast(getString(R.string.song_next, musicPlayer?.currentPlayMusic()?.name))
    }

    private fun View.updatePlayMusic() {
        val currentPlayMusic = musicPlayer?.currentPlayMusic() ?: return
        binMusicLayout.nameListeningTextView.text = currentPlayMusic.name
        nameMusicTextView.text = currentPlayMusic.name
        artistNameTextView.text = currentPlayMusic.artistName
        endTimeTextView.text = currentPlayMusic.duration.toString()
        musicIconImageView.loadFromUrl(currentPlayMusic.image)
        binMusicLayout.playImageView.setBackgroundStartMusic()
    }

    private fun ImageView.setBackgroundStartMusic() {
        setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
    }

    private fun ImageView.setBackgroudPauseMusic() {
        setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
    }

    companion object {
        fun newInstance() = PlayFragment()
    }
}
