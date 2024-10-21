function showImageModal(imagePath, inputKeyword , inputDate) {
    var modal = document.getElementById("imageModal");
    var modalImg = document.getElementById("modalImage");
    var modalInputDate = document.getElementById("inputDate");
    var modalInputKeyword = document.getElementById("inputKeyword");

    modalImg.src = 'http://localhost:8080/admin/employees/files/' + imagePath;
    modalInputKeyword.textContent = inputKeyword;

    if (typeof inputDate === 'string' && inputDate.trim() !== '') {
        var date = new Date(inputDate);

        var formattedDate = ('0' + date.getDate()).slice(-2) + '/' +
            ('0' + (date.getMonth() + 1)).slice(-2) + '/' +
            date.getFullYear() + ' ' +
            ('0' + date.getHours()).slice(-2) + ':' +
            ('0' + date.getMinutes()).slice(-2);

        modalInputDate.textContent = formattedDate;
    } else {
        modalInputDate.textContent = "Không có ngày";
    }

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
