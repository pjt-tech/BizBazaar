// 탭 클릭 시 호출되는 함수
function showTab(tabName) {
    // 모든 탭 내용 숨기기
    const tabs = document.querySelectorAll('.tab-content');
    tabs.forEach(tab => {
        tab.style.display = 'none';
    });

    // 탭 버튼 비활성화
    const tabButtons = document.querySelectorAll('.tab-button');
    tabButtons.forEach(button => {
        button.classList.remove('active');
    });

    // 클릭된 탭 버튼 활성화
    document.getElementById(tabName + 'TabButton').classList.add('active');

    // 클릭된 탭 내용 표시
    document.getElementById(tabName + 'Tab').style.display = 'block';

    // 해당 탭에 맞는 데이터 AJAX로 가져오기
    fetchTabData(tabName);
}

// AJAX로 데이터를 가져오는 함수
function fetchTabData(tabName) {
    let url = '';

    // 각 탭에 맞는 API URL 설정
    if (tabName === 'admin') {
        url = '/admin/admins';
    }
    else if (tabName === 'vendor') {
        url = '/admin/vendors';
    }
    else if (tabName === 'customer') {
        url = '/admin/customers';
    }

    // AJAX 요청
    $.ajax({
        url: url,  // 해당 탭에 맞는 데이터를 가져오는 API
        method: 'GET',
        success: function(data) {
            updateTabContent(tabName, data);  // 데이터를 받아서 탭에 맞게 갱신
        },
        error: function(error) {
            console.error(tabName + ' 데이터 조회 오류:', error);
        }
    });
}

// 탭 내용을 업데이트하는 함수
function updateTabContent(tabName, data) {
    const listContainer = document.getElementById(tabName + 'List');
    listContainer.innerHTML = '';  // 기존 내용을 비우고

    data.forEach(item => {
        let listItem = document.createElement('li');

        // name과 email만 출력하고 줄바꿈 추가
        let nameElement = document.createElement('div');
        nameElement.textContent = `이름: ${item.name || '이름 없음'}`;
        listItem.appendChild(nameElement);

        let emailElement = document.createElement('div');
        emailElement.textContent = `이메일: ${item.email || '이메일 없음'}`;
        listItem.appendChild(emailElement);

        listContainer.appendChild(listItem);
    });
}
