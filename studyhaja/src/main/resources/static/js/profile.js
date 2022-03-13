$(function () {
   cropper = '';
   let $confirmBtn = $('#confirm-button'); // 확인 버튼
   let $resetBtn = $('#reset-button'); // 리셋 버튼
   let $cutBtn = $('#cut-button'); // 자르기 버튼
   let $newProfileImage = $('#new-profile-image'); // 새로운 프로필 이미지
   let $currentProfileImage = $('#current-profile-image'); // 현재 프로필 이미지
   let $resultImage = $('#cropped-new-profile-image'); // 선택한 이미지중 잘라낸 영역 이미지
   let $profileImage = $('#profileImage');

   $newProfileImage.hide();
   $cutBtn.hide();
   $resetBtn.hide();
   $confirmBtn.hide();

   $('#profile-image-file').change(function (e) {
      if (e.target.files.length === 1) {
         const reader = new FileReader();
         reader.onload = e => {
            if (e.target.result) {
               if (!e.target.result.startsWith("data:image")) {
                  alert("이미지 파일을 선택하세요.");
                  return;
               }

               let img = document.createElement('img');
               img.id = 'new-profile';
               img.src = e.target.result;
               img.setAttribute('width', '100%');

               $newProfileImage.html(img);
               $newProfileImage.show();
               $currentProfileImage.hide();

               let $newImage = $(img);
               $newImage.cropper({aspectRatio: 1});
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
      $currentProfileImage.show();
      $newProfileImage.hide();
      $resultImage.hide();
      $resetBtn.hide();
      $cutBtn.hide();
      $confirmBtn.hide();
      $profileImage.val('');
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
      newImage.width = 125;

      $resultImage.html(newImage);
      $resultImage.show();
      $confirmBtn.show();

      $confirmBtn.click(function () {
         $newProfileImage.html(newImage);
         $cutBtn.hide();
         $confirmBtn.hide();
         $profileImage.val(dataUrl);
      });
   });
});