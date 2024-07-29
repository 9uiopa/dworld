//댓글 추가
const commentArea = document.getElementById('comment-area');
commentArea.addEventListener('click',ev => {
    if (ev.target && ev.target.classList.contains('submit-comment-btn')) {// 댓글 달기 버튼 눌렀을 때
        let articleId = document.getElementById('article-id').value;
        const commentForm = ev.target.closest('.comment-form');
        const textarea = commentForm.querySelector('.comment-textarea');
        const author = commentForm.querySelector('.comment-form-author').value;
        const parentComment = commentForm.closest('.comment');
        let parentCommentId = null;
        if(parentComment){
            parentCommentId = parentComment.getAttribute('data-comment-id')
        }

        fetch(`/api/articles/${articleId}/comments`,{
            method:'POST',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                content: textarea.value,
                author: author,
                parentCommentId: parentCommentId
            })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                return response.json(); // 응답 객체를 JSON 형태(javascript 객체)로 변환
            })
            .then(data => {
                location.reload();

            })
            .catch(error => {
                console.error('There has been a problem with your fetch operation - comment:', error);
                alert('댓글 등록에 실패했습니다. 다시 시도해주세요.');
            });

    }
})

//대댓글 폼 추가
const replyButtons = document.querySelectorAll('.comment-form-btn');
replyButtons.forEach(button => {
    button.addEventListener('click', function() {
        const commentDiv = button.closest('.comment'); // 부모 요소 (댓글 컨테이너)를 찾음
        const existingReplyForm = commentDiv.querySelector('.comment-form');
        if (!existingReplyForm) { // 기존의 답글 폼이 없으면 새로 추가
            // 답글 폼 생성
            const commentForm = document.createElement('div');
            commentForm.classList.add('comment-form', 'mt-2');

            const label = document.createElement('label');
            label.setAttribute('for',`content-${commentDiv.id}`);

            const input = document.createElement('input');
            input.type = 'hidden';
            input.classList.add('comment-form-author');
            input.value = '오유리';

            const textarea = document.createElement('textarea');
            textarea.classList.add('form-control','comment-textarea');
            textarea.id = `content-${commentDiv.id}`;
            textarea.rows = 3;
            textarea.placeholder = '댓글을 입력하세요';

            const submitButton = document.createElement('button');
            submitButton.type = 'button';
            submitButton.textContent = '댓글 달기';
            submitButton.classList.add('btn', 'btn-primary', 'mt-2','submit-comment-btn');
            submitButton.id = `submit-comment-btn-${commentDiv.id}`;

            commentForm.appendChild(label);
            commentForm.appendChild(input);
            commentForm.appendChild(textarea);
            commentForm.appendChild(submitButton);


            // 댓글 컨테이너에 답글 폼 추가
            commentDiv.appendChild(commentForm);
        }else{ // 이미 폼이 있으면 제거
            existingReplyForm.remove();
        }
    });
});

//시간
// function timeForToday(value) {
//     const today = new Date();
//     const timeValue = new Date(value);
//     const betweenTime = Math.floor((today.getTime() - timeValue.getTime()) / 1000 / 60);
//
//     if (betweenTime < 1) return '방금전';
//     if (betweenTime < 60) {
//         return `\${betweenTime}분전`;
//     }
//
//     const betweenTimeHour = Math.floor(betweenTime / 60);
//     if (betweenTimeHour < 24) {
//         return `\${betweenTimeHour}시간전`;
//     }
//
//     const betweenTimeDay = Math.floor(betweenTime / 60 / 24);
//     if (betweenTimeDay < 365) {
//         return `\${betweenTimeDay}일전`;
//     }
//
//     return `\${Math.floor(betweenTimeDay / 365)}년전`;
// }
