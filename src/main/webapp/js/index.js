var indexVue = new Vue({
    el: '#indexDiv',
    data: {
        username: '',
        realname: '',
        avatar: ''
    },
    mounted: function () {//加载页面自动执行
        this.getUser();
    },
    methods: {
        getUser: function () {//获取登录用户信息
            var user = JSON.parse(sessionStorage.getItem("user"));//将字符串解析为JSON格式
            this.username = user.username;
            this.avatar = user.avatar;
            this.realname = user.realname;
        }
    }
});