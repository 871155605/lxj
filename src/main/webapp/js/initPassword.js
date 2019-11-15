var initPasswordVue = new Vue({
    el: '#initPasswordDiv',
    data: {
        initPassword: '',
        newPassword: '',
        responseMsg: ''
    },
    methods: {
        inItPasswordFunction: function () {
            var salt = "bsmg";//使用固定的salt
            var nPassword = JSON.parse(JSON.stringify(this.newPassword));//深克隆获得用户输入的密码，用于加密
            var itPassword = JSON.parse(JSON.stringify(this.initPassword));
            this.newPassword = md5(salt.charAt(3) + salt.charAt(2) + nPassword + salt.charAt(0) + salt.charAt(1));
            this.initPassword = md5(salt.charAt(3) + salt.charAt(2) + itPassword + salt.charAt(0) + salt.charAt(1));
            axios.post('/user/initPassword', initPasswordVue.$data).then(function (response) {
                alert(response.data.message);
                initPasswordVue.responseMsg = response.data.data;
            }).catch(function (error) {
                console.log(error);
            });
        }
    }
});

