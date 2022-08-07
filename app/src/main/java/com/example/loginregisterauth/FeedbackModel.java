package com.example.loginregisterauth;

    import java.io.Serializable;

    public class FeedbackModel implements Serializable {
        public String useremail, usercomment;

        public FeedbackModel() {
        }

        public FeedbackModel(String useremail, String usercomment) {
            this.useremail = useremail;
            this.usercomment = usercomment;
        }

        public String getUseremail() {
            return useremail;
        }

        public void setUseremail(String useremail) { this.useremail = useremail; }

        public String getUsercomment() { return usercomment; }

        public void setUsercomment(String usercomment) { this.usercomment = usercomment; }

    }
