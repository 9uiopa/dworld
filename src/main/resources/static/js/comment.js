//댓글 추가
const commentArea = document.getElementById('comment-area');
const commentsSection = document.getElementById('comments-section');
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
                author: document.getElementById('main-comment-form').value,
                parentCommentId: parentCommentId
            })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                return response.text(); // JSON 파싱 전 원본 응답 출력
            })
            .then(data => {
                console.log("Raw Response:", data); // 서버 응답을 로그에 출력
                const parsedData = JSON.parse(data); // JSON 파싱 시도
                console.log("Parsed Data:", parsedData); // 파싱된 데이터 확인
                location.reload();

            })
            .catch(error => {
                console.error('There has been a problem with your fetch operation - comment:', error);
                alert('댓글 등록에 실패했습니다. 다시 시도해주세요.');
            });

    }
})

//댓글 삭제
commentArea.addEventListener('click',ev => {
    if (ev.target && ev.target.classList.contains('comment-delete-btn')) {// 댓글 삭제 버튼 눌렀을 때
        const targetComment = ev.target.closest('.comment');
        const commentId = targetComment.getAttribute('data-comment-id')
        fetch(`/api/comments/${commentId}`,{
            method:'PATCH',
            headers: {
                "Content-Type": "application/json",
            }
        })
            .then(response => {
                location.reload();
            })
            .catch(error => {
                console.error('There has been a problem with your fetch operation - delete comment:', error);
                alert('댓글 삭제에 실패했습니다. 다시 시도해주세요.');
            });

    }
})

//대댓글 폼 추가
const childCommentsButtons = document.querySelectorAll('.comment-form-btn');

childCommentsButtons.forEach(button => {
    button.addEventListener('click', function() {
        const commentDiv = button.closest('.comment'); // 부모 요소 (댓글 컨테이너)를 찾음
        const childCommentDiv = commentDiv.querySelector('.childComment');
        const existingReplyForm = commentDiv.querySelector('.comment-form');
        if (!existingReplyForm) { // 기존의 답글 폼이 없으면 새로 추가
            //다른 답글 폼 제거 (답글 폼은 1개만 표시된다)
            commentsSection.querySelectorAll('.comment-form').forEach(element => element.remove());
            // 답글 폼 생성
            const commentForm = document.createElement('div');
            commentForm.classList.add('comment-form', 'mt-2');

            const label = document.createElement('label');
            label.setAttribute('for',`content-${commentDiv.id}`);

            const input = document.createElement('input');
            input.type = 'hidden';
            input.classList.add('comment-form-author');
            input.value = document.getElementById('main-comment-form').value;

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

            //대댓글보다 앞쪽에 답글 폼 추가
            commentDiv.insertBefore(commentForm, childCommentDiv);
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
