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
        //切换登录方式
        changeLoginType: function (type) {
            this.loginType = type;
            if (type === 'user_password_realm') {
                this.checkCode = '';
            }
            if (type === 'user_phone_realm') {
                this.password = '';
            }
        },
        //登录请求
        login: function () {
            var salt = "bsmg";//使用固定的salt
            var password = JSON.parse(JSON.stringify(this.password));//深克隆获得用户输入的密码，用于加密
            this.password = md5(salt.charAt(3) + salt.charAt(2) + password + salt.charAt(0) + salt.charAt(1));
            axios.post('/user/login', loginVue.$data).then(function (response) {
                loginVue.responseMsg = response.data.message;
                if (response.data.code === 0) {
                    //JSON.stringify将JSON对象转换为字符串 因为sessionStorage.setItem只支持存储字符串格式
                    sessionStorage.setItem("user", JSON.stringify(response.data.data));
                    window.location.href = "/index.html";
                }
                loginVue.password = "";
            }).catch(function (error) {
                console.log(error);
            });
        },
        //是否第三方登录
        thirdPartLogin: function () {
            this.isThirdPartLogin = !this.isThirdPartLogin;
        },
        //获取验证码
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
        },
        //方便输错密码一键删除
        cleanUpThePassword: function () {
            this.password = '';
            this.checkCode = '';
        }
    }
});

