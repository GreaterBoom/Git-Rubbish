// API基础地址 - 通过Nginx转发
const API_BASE_URL = 'http://193.112.95.178/api';

// 配置axios默认设置
axios.defaults.baseURL = API_BASE_URL;
axios.defaults.timeout = 10000;

// 请求拦截器 - 自动添加JWT令牌
axios.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

// 响应拦截器 - 统一处理错误
axios.interceptors.response.use(
    response => {
        return response;
    },
    error => {
        if (error.response) {
            switch (error.response.status) {
                case 401:
                    showMessage('未登录或登录已过期', 'error');
                    localStorage.removeItem('token');
                    localStorage.removeItem('userInfo');
                    setTimeout(() => {
                        window.location.href = 'login.html';
                    }, 1500);
                    break;
                case 403:
                    showMessage('没有权限访问', 'error');
                    break;
                case 404:
                    showMessage('请求的资源不存在', 'error');
                    break;
                case 500:
                    showMessage('服务器内部错误', 'error');
                    break;
                default:
                    showMessage(error.response.data.message || '请求失败', 'error');
            }
        } else {
            showMessage('网络错误，请检查网络连接', 'error');
        }
        return Promise.reject(error);
    }
);

// 显示消息提示
function showMessage(message, type = 'info') {
    // 创建提示元素
    const messageDiv = document.createElement('div');
    messageDiv.className = `fixed top-4 right-4 px-6 py-3 rounded shadow-lg z-50 ${
        type === 'success' ? 'bg-green-500' : 
        type === 'error' ? 'bg-red-500' : 
        'bg-blue-500'
    } text-white font-bold`;
    messageDiv.textContent = message;
    
    document.body.appendChild(messageDiv);
    
    // 3秒后自动移除
    setTimeout(() => {
        messageDiv.remove();
    }, 3000);
}

// 检查登录状态
function requireLogin() {
    const token = localStorage.getItem('token');
    if (!token) {
        showMessage('请先登录', 'error');
        setTimeout(() => {
            window.location.href = 'login.html';
        }, 1500);
        return false;
    }
    return true;
}

// 获取当前用户信息
function getCurrentUser() {
    const userInfo = localStorage.getItem('userInfo');
    return userInfo ? JSON.parse(userInfo) : null;
}

// 退出登录
function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('userInfo');
    showMessage('退出登录成功', 'success');
    setTimeout(() => {
        window.location.href = 'login.html';
    }, 1000);
}

// 格式化日期
function formatDate(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}

// 格式化日期时间
function formatDateTime(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${year}-${month}-${day} ${hours}:${minutes}`;
}

// 获取URL参数
function getUrlParam(name) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
}