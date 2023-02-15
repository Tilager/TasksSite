$(document).on("click", ".remove-achievement", function () {
    let li = $(this).closest("li");
    let a = li.children("a");

    $.ajax({
        method: "post",
        url: "/api/removeAchievement",
        contentType: "text/plain;charset=utf-8",
        data: a.text(),
        success: function () {
            alert("Достижение успешно удалено!");
            li.remove();
        },
        error: function (err) {
            console.error(err);
        }
    })
})

$(document).on("click", ".remove-science", function () {
    let li = $(this).closest("li");
    let a = li.children("a");

    $.ajax({
        method: "post",
        url: "/api/removeScience",
        contentType: "text/plain;charset=utf-8",
        data: a.text(),
        success: function () {
            alert("Научная работа успешно удалена!");
            li.remove();
        },
        error: function (err) {
            console.error(err);
        }
    })
})

$(document).on("click", ".remove-course", function () {
    let li = $(this).closest("li");
    let a = li.children("a");

    $.ajax({
        method: "post",
        url: "/api/removeCourse",
        data: a.text(),
        contentType: "text/plain;charset=utf-8",
        success: function () {
            alert("Курс успешно удален!");
            li.remove();
        },
        error: function (err) {
            console.error(err);
        }
    })


})

$(document).on("click", "#add-achievement", function () {
    let inputFile = $("#add-achievement-input");
    inputFile.click();

    inputFile.change(function (e) {
        e.stopImmediatePropagation();
        let file = inputFile.prop('files')[0];
        if (file.name.split('.').pop() !== 'pdf') {
            alert("Возможно загрузить только pdf файл!")
        } else if (confirm(`Вы уверены, что хотите загрузить: ${file.name}?`)) {
            let formData = new FormData();
            formData.append("file", file);

            $.ajax({
                url: '/api/uploadAchievement',
                enctype: 'multipart/form-data',
                method: "POST",
                data: formData,
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    alert("File uploaded!");
                    $("#achievements-list").append(`
                        <li>
                            <a href="/achievements/achievements/${data}">${data.substring(0, data.length - 4)}</a>
                            <button class="remove-achievement"><i class="fas fa-trash-alt" aria-hidden="true"></i></button>
                        </li>
                    `);
                    return false;
                },
                error: function (err) {
                    console.error(err);
                }
            });

        } else {
            return false;
        }

        inputFile.val = "";
    })
})

$(document).on("click", "#add-course", function () {
    let inputFile = $("#add-course-input");
    inputFile.click();

    inputFile.change(function (e) {
        e.stopImmediatePropagation();
        let file = inputFile.prop('files')[0];
        if (file.name.split('.').pop() !== 'pdf') {
            alert("Возможно загрузить только pdf файл!")
        } else if (confirm(`Вы уверены, что хотите загрузить: ${file.name}?`)) {
            let formData = new FormData();
            formData.append("file", file);

            $.ajax({
                url: '/api/uploadCourse',
                enctype: 'multipart/form-data',
                method: "POST",
                data: formData,
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    alert("File uploaded!");
                    $("#courses-list").append(`
                        <li>
                            <a href="/achievements/courses/${data}">${data.substring(0, data.length - 4)}</a>
                            <button class="remove-course"><i class="fas fa-trash-alt" aria-hidden="true"></i></button>
                        </li>
                    `);
                    return false;
                },
                error: function (err) {
                    console.error(err);
                }
            });

        } else {
            return false;
        }

        inputFile.val = "";
    })
})

$(document).on("click", "#add-science", function () {
    let inputFile = $("#add-science-input");
    inputFile.click();

    inputFile.change(function (e) {
        e.stopImmediatePropagation();
        let file = inputFile.prop('files')[0];
        if (file.name.split('.').pop() !== 'pdf') {
            alert("Возможно загрузить только pdf файл!")
        } else if (confirm(`Вы уверены, что хотите загрузить: ${file.name}?`)) {
            let formData = new FormData();
            formData.append("file", file);

            $.ajax({
                url: '/api/uploadScience',
                enctype: 'multipart/form-data',
                method: "POST",
                data: formData,
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    alert("File uploaded!");
                    $("#sciences-list").append(`
                        <li>
                            <a href="/achievements/sciences/${data}">${data.substring(0, data.length - 4)}</a>
                            <button class="remove-science"><i class="fas fa-trash-alt" aria-hidden="true"></i></button>
                        </li>
                    `);
                    return false;
                },
                error: function (err) {
                    console.error(err);
                }
            });

        } else {
            return false;
        }

        inputFile.val = "";
    })
})