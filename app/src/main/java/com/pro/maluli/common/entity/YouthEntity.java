package com.pro.maluli.common.entity;

import java.io.Serializable;

public class YouthEntity implements Serializable {

    /**
     * member : {"is_teenager":0,"is_login":1,"is_set_teenager_password":0}
     * top : {"desc":"&lt;p&gt;内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容&lt;/p&gt;\n","info":"&lt;p&gt;内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容&lt;/p&gt;\n\n&lt;p&gt;&lt;img alt=&quot;&quot; src=&quot;https://zhibo-1258651624.cos.ap-guangzhou.myqcloud.com/upload/20210727/0256aa8432ee428875d67c31ef3346a4.jpg&quot; style=&quot;height:519px; width:400px&quot; /&gt;&lt;/p&gt;\n","find_password":"&lt;h1&gt;密码指引内容&lt;/h1&gt;\n\n&lt;ul&gt;\n\t&lt;li&gt;系统默认内置了&lt;code&gt;ckeditor4&lt;/code&gt;编辑器, 只需要简单的初始化操作就可以轻松使用富文本编辑器。&lt;/li&gt;\n\t&lt;li&gt;在&lt;code&gt;class&lt;/code&gt;引入&lt;code&gt;editor&lt;/code&gt;样式即可完成初始化操作。&lt;/li&gt;\n\t&lt;li&gt;可以使用&lt;code&gt;rows&lt;/code&gt;来控制编辑器的高度。&lt;/li&gt;\n&lt;/ul&gt;\n\n&lt;blockquote&gt;\n&lt;p&gt;代码示例&lt;/p&gt;\n&lt;/blockquote&gt;\n\n&lt;pre&gt;\n&lt;code&gt;&amp;lt;div class=&amp;quot;layui-form-item&amp;quot;&amp;gt;\n    &amp;lt;label class=&amp;quot;layui-form-label&amp;quot;&amp;gt;商品描述&amp;lt;/label&amp;gt;\n    &amp;lt;div class=&amp;quot;layui-input-block&amp;quot;&amp;gt;\n        &amp;lt;textarea name=&amp;quot;describe&amp;quot; rows=&amp;quot;20&amp;quot; class=&amp;quot;layui-textarea editor&amp;quot; placeholder=&amp;quot;请输入商品描述&amp;quot;&amp;gt;{$row.describe|default=&amp;#39;&amp;#39;}&amp;lt;/textarea&amp;gt;\n    &amp;lt;/div&amp;gt;\n&amp;lt;/div&amp;gt;&lt;/code&gt;&lt;/pre&gt;\n"}
     * bottom : {"title":"中共中央举办新闻发布会，介绍中国共产党成立1 00周年庆祝活动安排","content":"&lt;p&gt;标题&lt;/p&gt;\n\n&lt;p&gt;内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容&lt;/p&gt;\n","image":"https://zhibo-1258651624.cos.ap-guangzhou.myqcloud.com/upload/20210706/8c25f8b2d7e50265f481e5225c270f26.jpg"}
     */

    private MemberBean member;
    private TopBean top;
    private BottomBean bottom;

    public MemberBean getMember() {
        return member;
    }

    public void setMember(MemberBean member) {
        this.member = member;
    }

    public TopBean getTop() {
        return top;
    }

    public void setTop(TopBean top) {
        this.top = top;
    }

    public BottomBean getBottom() {
        return bottom;
    }

    public void setBottom(BottomBean bottom) {
        this.bottom = bottom;
    }

    @Override
    public String toString() {
        return "YouthEntity{" +
                "member=" + member +
                ", top=" + top +
                ", bottom=" + bottom +
                '}';
    }

    public static class MemberBean implements Serializable {

        /**
         * is_teenager : 0
         * is_login : 1
         * is_set_teenager_password : 0
         */
        private int is_ban;
        private int is_teenager;
        private int is_login;
        private int is_set_teenager_password;

        public int getIs_ban() {
            return is_ban;
        }

        public void setIs_ban(int is_ban) {
            this.is_ban = is_ban;
        }

        public int getIs_teenager() {
            return is_teenager;
        }

        public void setIs_teenager(int is_teenager) {
            this.is_teenager = is_teenager;
        }

        public int getIs_login() {
            return is_login;
        }

        public void setIs_login(int is_login) {
            this.is_login = is_login;
        }

        public int getIs_set_teenager_password() {
            return is_set_teenager_password;
        }

        public void setIs_set_teenager_password(int is_set_teenager_password) {
            this.is_set_teenager_password = is_set_teenager_password;
        }

        @Override
        public String toString() {
            return "MemberBean{" +
                    "is_ban=" + is_ban +
                    ", is_teenager=" + is_teenager +
                    ", is_login=" + is_login +
                    ", is_set_teenager_password=" + is_set_teenager_password +
                    '}';
        }
    }

    public static class TopBean implements Serializable {
        /**
         * desc : &lt;p&gt;内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容&lt;/p&gt;
         * info : &lt;p&gt;内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容&lt;/p&gt;
         * <p>
         * &lt;p&gt;&lt;img alt=&quot;&quot; src=&quot;https://zhibo-1258651624.cos.ap-guangzhou.myqcloud.com/upload/20210727/0256aa8432ee428875d67c31ef3346a4.jpg&quot; style=&quot;height:519px; width:400px&quot; /&gt;&lt;/p&gt;
         * find_password : &lt;h1&gt;密码指引内容&lt;/h1&gt;
         * <p>
         * &lt;ul&gt;
         * &lt;li&gt;系统默认内置了&lt;code&gt;ckeditor4&lt;/code&gt;编辑器, 只需要简单的初始化操作就可以轻松使用富文本编辑器。&lt;/li&gt;
         * &lt;li&gt;在&lt;code&gt;class&lt;/code&gt;引入&lt;code&gt;editor&lt;/code&gt;样式即可完成初始化操作。&lt;/li&gt;
         * &lt;li&gt;可以使用&lt;code&gt;rows&lt;/code&gt;来控制编辑器的高度。&lt;/li&gt;
         * &lt;/ul&gt;
         * <p>
         * &lt;blockquote&gt;
         * &lt;p&gt;代码示例&lt;/p&gt;
         * &lt;/blockquote&gt;
         * <p>
         * &lt;pre&gt;
         * &lt;code&gt;&amp;lt;div class=&amp;quot;layui-form-item&amp;quot;&amp;gt;
         * &amp;lt;label class=&amp;quot;layui-form-label&amp;quot;&amp;gt;商品描述&amp;lt;/label&amp;gt;
         * &amp;lt;div class=&amp;quot;layui-input-block&amp;quot;&amp;gt;
         * &amp;lt;textarea name=&amp;quot;describe&amp;quot; rows=&amp;quot;20&amp;quot; class=&amp;quot;layui-textarea editor&amp;quot; placeholder=&amp;quot;请输入商品描述&amp;quot;&amp;gt;{$row.describe|default=&amp;#39;&amp;#39;}&amp;lt;/textarea&amp;gt;
         * &amp;lt;/div&amp;gt;
         * &amp;lt;/div&amp;gt;&lt;/code&gt;&lt;/pre&gt;
         */

        private String desc;
        private String info;
        private String find_password;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getFind_password() {
            return find_password;
        }

        public void setFind_password(String find_password) {
            this.find_password = find_password;
        }

        @Override
        public String toString() {
            return "TopBean{" +
                    "desc='" + desc + '\'' +
                    ", info='" + info + '\'' +
                    ", find_password='" + find_password + '\'' +
                    '}';
        }
    }

    public static class BottomBean implements Serializable {
        /**
         * title : 中共中央举办新闻发布会，介绍中国共产党成立1 00周年庆祝活动安排
         * content : &lt;p&gt;标题&lt;/p&gt;
         * <p>
         * &lt;p&gt;内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容&lt;/p&gt;
         * image : https://zhibo-1258651624.cos.ap-guangzhou.myqcloud.com/upload/20210706/8c25f8b2d7e50265f481e5225c270f26.jpg
         */

        private String title;
        private String content;
        private String image;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        @Override
        public String toString() {
            return "BottomBean{" +
                    "title='" + title + '\'' +
                    ", content='" + content + '\'' +
                    ", image='" + image + '\'' +
                    '}';
        }
    }
}