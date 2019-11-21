var indexUue = new Vue({
    el: '#indexDiv',
    data: {
        username: '',
        realname: '',
        avatar: ''
    },
    mounted: function () {
        this.getUser();
    },
    methods: {
        getUser: function () {
            var user = JSON.parse(sessionStorage.getItem("user"));//将字符串解析为JSON格式
            this.username = user.username;
            this.realname = user.realname;
            this.avatar = user.avatar;
        }
    }
});