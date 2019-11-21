var updateUserVue = new Vue({
    el: '#updateUserDiv',
    data: {
        username: '',
        password: '',
        realname: '',
        avatar: '',
        phone: '',
        email: '',
        sex: '',
        locked: '',
        checkUsernameModel: '',
        checkPasswordModel: '',
        checkRealNameModel: '',
        checkPhoneModel: '',
        checkEmailModel: '',
        responseMsg: ''
    },
    methods: {
        insertUser: function () {
            var yesPngUrl = 'picture/yes.png';
            if (this.checkRealNameModel === yesPngUrl && this.checkPasswordModel === yesPngUrl && this.checkUsernameModel === yesPngUrl) {
                var salt = "bsmg";//使用固定的salt
                var password = JSON.parse(JSON.stringify(this.password));//深克隆获得用户输入的密码，用于加密
                this.password = md5(salt.charAt(3) + salt.charAt(2) + password + salt.charAt(0) + salt.charAt(1));
                axios.post('/user/insertUser', updateUserVue.$data).then(function (response) {
                    if (response.data.code === 0) {
                        alert('添加用户成功：' + response.data.message);
                    }
                })
            } else {
                this.responseMsg = '添加失败，提交的数据格式不合法';
            }
        },
        checkFormParam: function (name) {
            var yesPngUrl = 'picture/yes.png';
            var noPngUrl = 'picture/no.png';
            switch (name) {
                case 'username':
                    this.checkPasswordAndUsernameReg(this.username) ? this.checkUsernameModel = yesPngUrl : this.checkUsernameModel = noPngUrl;
                    break;
                case 'password':
                    this.checkPasswordAndUsernameReg(this.password) ? this.checkPasswordModel = yesPngUrl : this.checkPasswordModel = noPngUrl;
                    break;
                case 'realname':
                    this.checkRealNameReg(this.realname) ? this.checkRealNameModel = yesPngUrl : this.checkRealNameModel = noPngUrl;
                    break;
                case 'phone':
                    this.checkPhone(this.phone) ? this.checkPhoneModel = yesPngUrl : this.checkPhoneModel = noPngUrl;
                    break;
                case 'email':
                    this.checkEmail(this.email) ? this.checkEmailModel = yesPngUrl : this.checkEmailModel = noPngUrl;
                    break;
            }
        },
        //鼠标离开清除提示图片 DELETE清除输入框
        cleanInputOrHint: function (name, area) {
            switch (name) {
                case 'username':
                    area === 'input' ? this.username = '' : this.checkPasswordAndUsernameReg(this.username) ? true : this.checkUsernameModel = '';
                    break;
                case 'password':
                    area === 'input' ? this.password = '' : this.checkPasswordAndUsernameReg(this.password) ? true : this.checkPasswordModel = '';
                    break;
                case 'realname':
                    area === 'input' ? this.realname = '' : this.checkRealNameReg(this.realname) ? true : this.checkRealNameModel = '';
                    break;
                case 'phone':
                    area === 'input' ? this.phone = '' : this.checkPhone(this.phone) ? true : this.checkPhoneModel = '';
                    break;
                case 'email':
                    area === 'input' ? this.email = '' : this.checkEmail(this.email) ? true : this.checkEmailModel = '';
                    break;
            }
        },
        checkPasswordAndUsernameReg: function (param) {
            var reg = /^[a-zA-Z0-9]{4,20}$/;
            return reg.test(param);
        },
        checkRealNameReg: function (param) {
            var reg = /^[a-zA-Z0-9]{1,20}$/;
            return reg.test(param);
        },
        checkPhone: function (param) {
            return true;
        },
        checkEmail: function (param) {
            return true;
        },
        //因为图片双向绑定一直无法显示成功，上传图片成功后调用此方法用来激活
        changeAvatarFunction: function (avatar) {
            this.avatar = avatar;
        },

        uploadAvatar: function () {
            var avatarFolder = "/user_avatar/";
            //只获取文件名
            //var avatar = document.getElementById('avatar').files[0].name;
            //alert(avatar);
            //获取带路径的文件名
            //document.getElementById('avatar').value
            //序列化表单，可序列化文件 new FormData($('#form')[0]))
            axios.post('user/uploadAvatar', new FormData($('#form')[0])).then(function (response) {
                var avatar = avatarFolder + response.data.data;
                updateUserVue.changeAvatarFunction(avatar);
            })
        }
    }
});