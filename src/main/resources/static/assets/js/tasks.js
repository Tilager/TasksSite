$(document).on('click', '#create-folder-btn', function (e) {
    $.post({
        url: "/api/addFolder",
        method: "post",
        contentType: "application/json",
        success: function (data) {
            $('#folders-div').append(`<div class="group" id="${data.uuid}">
                    <div class="group-title">
                        <div class="group-name add-remove-group-btns">
                            <input type="text" name="group-name" class="folder-name" value="${data.name}">
                            <button type="button"><i class="fas fa-folder-minus file-remove-btn"></i></button> <!-- Удалить папку -->
                            <button type="button" class="file-add-btn">
                                <i class="fa fa-file" aria-hidden="true"></i>
                            </button> <!-- Добавить файл -->
                            <input id="file-add-${data.uuid}"
                                   type="file" name="file-add" style="display: none;"
                                   class="file-add"/>
                        </div>
                    </div>
                    <div class="group-content"></div>
                </div>`)
        }
    })
});

$(document).on('click', '.file-add-btn', function (e) {
    let folderId = $(this).closest('.group').attr('id');

    let inputFile = $(`#file-add-${folderId}`);
    inputFile.click();

    inputFile.change(function (e) {
        e.stopImmediatePropagation();
        let file = inputFile.prop('files')[0];
        if (confirm(`Вы уверены, что хотите загрузить ${file.name}?`)) {
            let formData = new FormData();
            formData.append("file", file);
            formData.append("folderId", folderId)

            $.ajax({
                url: '/api/uploadFile',
                enctype: 'multipart/form-data',
                method: "POST",
                data: formData,
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    alert("Файл успешно загружен!");
                    console.log(data);

                    $(`#${folderId} .group-content`).append(`
                        <div class="group-content-file-content" id=${data.uuid} >
                            <img src="/assets/img/file-icon.png" alt="file" class="group-content-file">
                            <p class="group-content-file-name">${data.name.substring(0, 7)}...</p>
                            <div class="group-content-file__items">
                                <a href="/files/${data.directory.uuid}/${data.uuid}.${data.name.split('.').pop()}"
                                   download="${data.name}"><i class="fas fa-download"></i></a>
                                <a href="" class="remove-file"><i class="fas fa-trash-alt"></i></a>
                            </div>
                        </div>
                    `);

                    return false;
                },
                error: function (data) {
                    alert("Не удалось загрузить файл! Возможно файл много весит или данный файл уже существует.");
                }

            });

        } else {
            return false;
        }
        inputFile.val = "";
    })
});

$(document).on('click', '.file-remove-btn', function (e) {
    let folderId = $(this).closest('.group').attr('id');

    $.post({
        url: "/api/removeFolder",
        method: "post",
        contentType: "application/json",
        data: folderId,
        success: function (data) {
            $(`#${folderId}`).remove();
            alert("Папка успешно удалена!");
        }
    })
});

$(document).on('click', '.remove-file', function (e) {
    let folderId = $(this).closest('.group').attr('id');
    let fileId = $(this).closest('.group-content-file-content').attr('id');
    let data = new FormData();
    data.append("folderId", folderId);
    data.append("fileId", fileId);

    $.ajax({
        url: "/api/removeFile",
        method: "POST",
        data: data,
        processData: false,
        contentType: false,
        cache: false,
        success: function (data) {
            $(`#${fileId}`).remove();
            alert("Файл успешно удален!");
        }
    })

    return false;
});

$(document).on('keydown', '.folder-name', function (e) {
    if (e.keyCode === 13) {
        let name = $(this).val();
        let folderId = $(this).closest('.group').attr('id');
        let data = new FormData();
        data.append("folderId", folderId);
        data.append("name", name);

        $.ajax({
            url: "/api/rename",
            method: "POST",
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            success: function (data) {
                alert("Папка успешно переименована!");
                let newFolderId = data.uuid;
                let folderDiv = $(`#${folderId}`);
                folderDiv.attr('id', newFolderId);

                let titleDiv = folderDiv.find(".group-title");
                titleDiv.find(".group-name").remove();
                titleDiv.append(`<div class="group-name add-remove-group-btns">
                                    <input type="text" name="group-name" class="folder-name" value="${data.name}">
                                    <button type="button" class="file-remove-btn"><i class="fas fa-folder-minus"></i></button> <!-- Удалить папку -->
                                    <button type="button" class="file-add-btn"><i class="fa fa-file" aria-hidden="true"></i></button> <!-- Добавить файл -->
                                    <input id="${'file-add-' + newFolderId}"
                                           type="file" name="file-add" style="display: none;"
                                           class="file-add"/>
                                </div>`);

                console.log();

            },
            statusCode: {
                400: function (data) {
                    if (data.responseText === "Folder not renamed.") {
                        alert("Папка не была переименована. (Возможно такая папка уже существует)")
                    }
                }
            }
        })
    }
})