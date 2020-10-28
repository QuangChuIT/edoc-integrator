window.localEdocStorage = {
    attachmentsId: "attachments",
    saveAttachments: function (object) {
        let instance = this;
        let objString = JSON.stringify(object);
        localStorage.setItem(instance.attachmentsId, objString);
    },
    getAttachments: function () {
        let instance = this;
        return JSON.parse(localStorage.getItem(instance.attachmentsId));
    },
    clearAttachments: function () {
        let instance = this;
        localStorage.removeItem(instance.attachmentsId);
    }
}

window.attachmentManager = {
    items: [],
    getItems: function () {
        return this.items;
    },
    setItems: function (items) {
        this.items = items;
    },
    addItem: function (item) {
        if (this.containsItem(item.attachmentId) === false) {
            this.items.push(item);
            window.localEdocStorage.saveAttachments(this.items);
        }
    },
    containsItem: function (attachmentId) {
        if (this.items === undefined) {
            return false;
        }
        for (let i = 0; i < this.items.length; i++) {

            let _item = this.items[i];

            if (attachmentId === _item.attachmentId) {
                return true;
            }
        }
        return false;
    },
    clearItems: function () {
        this.items = [];
    },
    deleteItem: function (attachmentId) {
        if (this.containsItem(attachmentId) === true) {
            let position = -1;
            for (let i = 0; i < this.items.length; i++) {
                let _item = this.items[i];
                if (attachmentId === _item.attachmentId) {
                    position = i;
                    break;
                }
            }
            if (position >= 0) {
                this.items.splice(position, 1);
            }
        }
    }
}
$(document).on("change", "#uploadFile", function (event) {
    //stop submit the form, we will post it manually.
    event.preventDefault();
    let form = $('#formAddEdoc')[0];
    let data = new FormData(form);
    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/attachment/-/document/upload",
        data: data,
        processData: false, //prevent jQuery from automatically transforming the data into a query string
        contentType: false,
        cache: false,
        success: (data) => {
            console.log(data);
            let attachmentsStore = window.localEdocStorage.getAttachments();
            if (attachmentsStore != null) {
                window.attachmentManager.setItems(attachmentsStore);
            } else {
                window.attachmentManager.setItems([]);
            }
            data.forEach(att => {
                window.attachmentManager.addItem(att);
            });
            window.localEdocStorage.saveAttachments(window.attachmentManager.getItems());
            let attachments = {"attachments": window.attachmentManager.getItems()};
            $("#attachment-content").empty();
            $("#edocAttachmentTemplate").tmpl(attachments).appendTo("#attachment-content");
            $("#uploadFile").val('');
        },
        error: (e) => {
            console.log(e.responseText);
        }
    });
});
$(document).on("click", "#downloadAllAttachment", function (event) {
    event.preventDefault();
    let attachmentsOfDoc = localStorage.getObj("attachmentsOfDoc");
    if (attachmentsOfDoc != null) {
        downloadAll(attachmentsOfDoc);
    }
});

function downloadAll(urls) {
    let link = document.createElement('a');
    link.style.display = 'none';
    document.body.appendChild(link);
    let urlDownload = "/attachment/-/download/";
    urls.forEach(function (element) {
        let href = urlDownload + element.attachmentId;
        link.setAttribute('href', href);
        link.setAttribute('download', element.name);
        link.click();
    });
    document.body.removeChild(link);
}


$(document).on("click", ".delete-attachment", function (event) {
    event.preventDefault();
    let attachmentIdVal = $(this).attr("data-id");
    if (attachmentIdVal !== "") {
        let attachmentId = parseInt(attachmentIdVal);
        $.ajax({
            type: "DELETE",
            url: "/attachment/-/delete/" + attachmentId,
            cache: false,
            success: (data) => {
                console.log(data);
                if (data.code === 200) {
                    let attachmentsStore = window.localEdocStorage.getAttachments();
                    window.attachmentManager.setItems(attachmentsStore);
                    console.log(window.attachmentManager.getItems());
                    window.attachmentManager.deleteItem(attachmentId);
                    console.log("after delete");
                    console.log(window.attachmentManager.getItems());
                    window.localEdocStorage.saveAttachments(window.attachmentManager.getItems());
                    let attToView = {"attachments": window.attachmentManager.getItems()};
                    $("#attachment-content").empty();
                    $("#edocAttachmentTemplate").tmpl(attToView).appendTo("#attachment-content");
                    $.notify(data.message, "success");
                } else {
                    $.notify(data.message, "error");
                }
            },
            error: (e) => {
                $.notify(e.responseText, "error");
            }
        });
    }

});

function bytesToSize(bytes) {
    let sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
    if (bytes === 0) return '0 Byte';
    let i = parseInt(Math.floor(Math.log(bytes) / Math.log(1024)));
    return Math.round(bytes / Math.pow(1024, i), 2) + ' ' + sizes[i];
}

Storage.prototype.setObj = function (key, obj) {
    return this.setItem(key, JSON.stringify(obj))
}
Storage.prototype.getObj = function (key) {
    return JSON.parse(this.getItem(key))
}