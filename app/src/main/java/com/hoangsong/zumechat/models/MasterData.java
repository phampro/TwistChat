package com.hoangsong.zumechat.models;

import java.util.ArrayList;

/**
 * Created by Tang on 17/10/2016.
 */

public class MasterData {
    private AppContent app_content;

    public MasterData(AppContent app_content) {
        this.app_content = app_content;
    }

    public AppContent getApp_content() {
        return app_content;
    }

    public void setApp_content(AppContent app_content) {
        this.app_content = app_content;
    }

    public static class AppContent{
        private String terms_of_use;
        private String private_policy;

        public AppContent(String terms_of_use, String private_policy) {
            this.terms_of_use = terms_of_use;
            this.private_policy = private_policy;
        }

        public String getTerms_of_use() {
            return terms_of_use;
        }

        public void setTerms_of_use(String terms_of_use) {
            this.terms_of_use = terms_of_use;
        }

        public String getPrivate_policy() {
            return private_policy;
        }

        public void setPrivate_policy(String private_policy) {
            this.private_policy = private_policy;
        }
    }
}
