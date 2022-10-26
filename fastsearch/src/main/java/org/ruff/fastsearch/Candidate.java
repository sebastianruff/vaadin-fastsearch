package org.ruff.fastsearch;

public class Candidate {

        private String id;
        private String content;

        public static final String ID = "id";
        public static final String CONTENT = "content";
        public static final String TAG = "tag";

        public Candidate() {
        }

        public Candidate(String i, String content) {
                this.id = i;
                this.content = content;
        }

        public String getId() {
                return id;
        }

        public void setId(String id) {
                this.id = id;
        }

        public String getContent() {
                return content;
        }

        public void setContent(String content) {
                this.content = content;
        }
}