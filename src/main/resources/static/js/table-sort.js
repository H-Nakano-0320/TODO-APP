$(document).ready( function () {
    $('#table_id').DataTable({

        // 表示件数のデフォルトを10件から25件に変更
        "pageLength" : 25,

        // "entries per page" を "表示件数" に変更
        // ページネーションを日本語に変更
         "language": {
            "url": "//cdn.datatables.net/plug-ins/1.13.6/i18n/ja.json"
         },

        // 検索ボックスの削除
        "searching" : false,

        // infoを削除
        "info" : false,

        // 初期化後にマージンを設定する
        "initComplete" : function(){
            $('.dt-length').addClass('mt-3');
        },
        "columnDefs": [
            {
                "className": "text-start",
                "targets": "_all"
            }
        ]
    });
});