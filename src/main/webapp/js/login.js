var loginVue = new Vue({
    el: '#loginDiv',
    data: {
        loginType: 'user_password_realm',
        username: '',
        password: '',
        phoneNumber: '',
        checkCode: '',
        isThirdPartLogin: false,
        remembered: false,
        responseMsg: ''
    },
    methods: {
        changeLoginType: function (type) {
            this.loginType = type;
            if (type === 'user_password_realm') {
                this.checkCode = '';
            }
            if (type === 'user_phone_realm') {
                this.password = '';
            }
        },
        login: function () {
            axios.post('/user/login', loginVue.$data).then(function (response) {
                if (response.data.code === -1) {
                    alert(response.data.message);
                }
                if (response.data.code === 0) {
                    alert('登录成功：' + response.data.data.realname);
                    window.location.href = "/index.html";
                }
            }).catch(function (error) {
                console.log(error);
            });
        },
        thirdPartLogin: function () {
            this.isThirdPartLogin = !this.isThirdPartLogin;
        },
        getCheckCode: function () {
            axios.post('/checkCode/getCheckCode', this.phoneNumber).then(function (response) {
                if (response.data.code === -1) {
                    alert(response.data.message);
                }
                if (response.data.code === 0) {
                    alert(response.data.message);
                }
            }).catch(function (error) {
                console.log(error);
            });
        }
    }
});
