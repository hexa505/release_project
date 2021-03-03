package com.project.release.service.event;

import com.project.release.domain.album.Album;
import com.project.release.domain.album.Comment;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AlbumEvent {

    @Getter
    public static class AlbumUpdatedEvent{
        private Album album;
        public AlbumUpdatedEvent(Album album) {
            this.album = album;
        }
    }

    @Getter
    public static class CommentCreatedEvent{
        private Album album;
        private Comment comment;
        public CommentCreatedEvent(Album album, Comment comment) {
            this.album = album;
            this.comment = comment;
        }
    }


}
