// 탭 버튼들
const adminTabButton = document.getElementById('adminTabButton');
const vendorTabButton = document.getElementById('vendorTabButton');
const customerTabButton = document.getElementById('customerTabButton');

// 탭들
const adminTab = document.getElementById('adminTab');
const vendorTab = document.getElementById('vendorTab');
const customerTab = document.getElementById('customerTab');

// 목록 요소들
const adminListElement = document.getElementById('adminList');
const vendorListElement = document.getElementById('vendorList');
const customerListElement = document.getElementById('customerList');

// 탭을 표시하는 함수
function showTab(tabId) {
    // 모든 탭 숨기기
    adminTab.style.display = 'none';
    vendorTab.style.display = 'none';
    customerTab.style.display = 'none';

    // 모든 탭 버튼 비활성화
    adminTabButton.classList.remove('active');
    vendorTabButton.classList.remove('active');
    customerTabButton.classList.remove('active');

    // 선택된 탭 표시
    tabId.style.display = 'block';
}

// 탭 버튼 클릭 시 해당하는 탭 표시
adminTabButton.addEventListener('click', () => {
    showTab(adminTab);
    adminTabButton.classList.add('active');
    fetchAdminData();  // 관리자 목록 가져오기
});

vendorTabButton.addEventListener('click', () => {
    showTab(vendorTab);
    vendorTabButton.classList.add('active');
    fetchVendorData();  // 벤더 목록 가져오기
});

customerTabButton.addEventListener('click', () => {
    showTab(customerTab);
    customerTabButton.classList.add('active');
    fetchCustomerData();  // 고객 목록 가져오기
});

// 데이터 가져오는 함수들
function fetchAdminData() {
    fetch('/api/admins')  // 관리자 목록을 가져오는 API 호출
        .then(response => response.json())
        .then(data => {
            updateList(adminListElement, data);
        })
        .catch(error => {
            console.error('Error fetching admin data:', error);
        });
}

function fetchVendorData() {
    fetch('/api/vendors')  // 벤더 목록을 가져오는 API 호출
        .then(response => response.json())
        .then(data => {
            updateList(vendorListElement, data);
        })
        .catch(error => {
            console.error('Error fetching vendor data:', error);
        });
}

function fetchCustomerData() {
    fetch('/api/customers')  // 고객 목록을 가져오는 API 호출
        .then(response => response.json())
        .then(data => {
            updateList(customerListElement, data);
        })
        .catch(error => {
            console.error('Error fetching customer data:', error);
        });
}

// 목록을 동적으로 업데이트하는 함수
function updateList(listElement, data) {
    listElement.innerHTML = '';  // 기존 목록 비우기
    data.forEach(item => {
        const listItem = document.createElement('li');
        listItem.textContent = item.name;  // 예시로 'name' 속성을 표시
        listElement.appendChild(listItem);
    });
}

// 처음에 관리자 탭을 기본으로 활성화
showTab(adminTab);
adminTabButton.classList.add('active');
fetchAdminData();  // 기본 관리자 목록 표시
