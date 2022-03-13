$(function () {
    cropper = '';
    let $confirmBtn = $('#confirm-button');
    let $resetBtn = $('#reset-button');
    let $cutBtn = $('#cut-button');
    let $saveBtn = $('#save-button')
    let $newStudyImage = $('#new-study-image');
    let $currentStudyImage = $('#current-study-image');
    let $resultImage = $('#cropped-new-study-image');
    let $studyImage = $('#studyImage');

    $newStudyImage.hide();
    $cutBtn.hide();
    $resetBtn.hide();
    $confirmBtn.hide();
    $saveBtn.hide();

    $('#study-image-file').change(function (e) {
        if (e.target.files.length === 1) {
            const reader = new FileReader();
            reader.onload = e => {
                if (e.target.result) {
                    if (!e.target.result.startsWith("data:image")) {
                        alert("이미지 파일을 선택하세요.");
                        return;
                    }

                    let img = document.createElement('img');
                    img.id = 'new-study';
                    img.src = e.target.result;
                    img.setAttribute('width', '100%');

                    $newStudyImage.html(img);
                    $newStudyImage.show();
                    $currentStudyImage.hide();

                    let $newImage = $(img);
                    $newImage.cropper({aspectRatio: 13/2});
                    cropper = $newImage.data('cropper');

                    $cutBtn.show();
                    $confirmBtn.hide();
                    $resetBtn.show();
                }
            };
            reader.readAsDataURL(e.target.files[0]);
        }
    });

    $resetBtn.click(function () {
        $currentStudyImage.show();
        $newStudyImage.hide();
        $resultImage.hide();
        $resetBtn.hide();
        $cutBtn.hide();
        $confirmBtn.hide();
        $saveBtn.hide();
        $studyImage.val('');
    });

    $cutBtn.click(function () {
        let dataUrl = cropper.getCroppedCanvas().toDataURL();

        if (dataUrl.length > 1000 * 1024) {
            alert("이미지 파일이 너무 큽니다. 102400 보다 작은 파일을 사용하세요. 현재 이미지 사이즈는 " + dataUrl.length);
            return;
        }

        let newImage = document.createElement('img');
        newImage.id = 'cropped-new-profile-image';
        newImage.src = dataUrl;
        newImage.width = 640;

        $resultImage.html(newImage);
        $resultImage.show();
        $confirmBtn.show();

        $confirmBtn.click(function () {
            $newStudyImage.html(newImage);
            $cutBtn.hide();
            $confirmBtn.hide();
            $studyImage.val(dataUrl);
            $saveBtn.show();
        });
    });

    $saveBtn.click(function () {
        $('#imageForm').submit();
    });
});