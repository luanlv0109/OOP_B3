function showImageModal(imagePath, inputKeyword) {
    var modal = document.getElementById("imageModal");
    var modalImg = document.getElementById("modalImage");
    var modalInputDate = document.getElementById("inputDate"); // Thêm biến cho ngày

    var modalInputKeyword = document.getElementById("inputKeyword");

    // Đặt thông tin vào các phần tử trong modal
    modalImg.src = 'http://localhost:8080/admin/employees/files/' + imagePath;
    modalInputKeyword.textContent = inputKeyword;
    modalInputDate.textContent = (typeof inputDate === 'string' && inputDate.trim() !== '') ? inputDate : "Không có ngày";

    console.log(imagePath)
    // Hiển thị modal
    modal.style.display = "flex";
}

var month = /*[[${selectedMonth}]]*/ 10;
var year = /*[[${selectedYear}]]*/ 2024;

var newPath = `/admin/search-result/list?month=${month}&year=${year}`;
window.history.pushState({}, '', newPath); // Cập nhật URL mà không reload lại trang

function closeModal() {
    var modal = document.getElementById("imageModal");
    if (modal) {
        modal.style.display = "none";
    }
}