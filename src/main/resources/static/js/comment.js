// 댓글 관련 이벤트 처리
const commentArea = document.querySelector('.comments-area');

commentArea.addEventListener('click', function(event) {
    // 댓글 달기 버튼 클릭 이벤트 처리
    if (event.target && event.target.id === 'submit-comment-btn') {
        let articleId = document.getElementById('article-id').value;
        let commentContent = document.getElementById('comment-content');
        fetch(`/api/articles/${articleId}/comments`, {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                content: commentContent.value,
                author: document.getElementById('comment-author').value,
                parentCommentId: null // 루트 댓글의 경우 parentCommentId는 null
            })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                const commentsSection = document.getElementById("comments-section");
                const newComment = document.createElement("p");
                newComment.innerHTML = `<strong>${data.user.username}</strong><span>${timeForToday(data.createdAt)}</span><br><span>${data.content}</span>`;
                commentsSection.appendChild(newComment);

                commentContent.value = "";
            })
            .catch(error => {
                console.error('There has been a problem with your fetch operation - comment:', error);
                alert('댓글 등록에 실패했습니다. 다시 시도해주세요.');
            });
    }

    // 대댓글 달기 버튼 클릭 이벤트 처리
    if (event.target && event.target.id.startsWith('submit-comment-btn-')) {
        const commentDiv = event.target.closest('.comment');
        const textarea = commentDiv.querySelector('textarea');
        let parentCommentId = commentDiv.getAttribute('data-comment-id');

        fetch(`/api/comments/${parentCommentId}/replies`, {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                content: textarea.value,
                author: document.getElementById('comment-author').value,
                parentCommentId: parentCommentId // 대댓글의 경우 parentCommentId가 필요
            })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                const replySection = commentDiv.querySelector('.replies-section') || document.createElement("div");
                replySection.classList.add('replies-section');

                const newReply = document.createElement("p");
                newReply.innerHTML = `<strong>${data.user.username}</strong><span>${timeForToday(data.createdAt)}</span><br><span>${data.content}</span>`;
                replySection.appendChild(newReply);
                commentDiv.appendChild(replySection);

                textarea.value = "";
            })
            .catch(error => {
                console.error('There has been a problem with your fetch operation - reply:', error);
                alert('대댓글 등록에 실패했습니다. 다시 시도해주세요.');
            });
    }

    // 대댓글 폼 추가/제거 처리
    if (event.target && event.target.classList.contains('comment-form-btn')) {
        const commentDiv = event.target.closest('.comment');
        const existingReplyForm = commentDiv.querySelector('.reply-form');

        if (existingReplyForm) {
            existingReplyForm.remove();
        } else {
            const commentForm = document.createElement('div');
            commentForm.classList.add('reply-form', 'mt-2');

            const label = document.createElement('label');
            label.setAttribute('for', `content-${commentDiv.id}`);
            label.textContent = '내용';

            const textarea = document.createElement('textarea');
            textarea.classList.add('form-control');
            textarea.id = `content-${commentDiv.id}`;
            textarea.rows = 3;
            textarea.placeholder = '댓글을 입력하세요';

            const submitButton = document.createElement('button');
            submitButton.type = 'button';
            submitButton.textContent = '댓글 달기';
            submitButton.classList.add('btn', 'btn-primary', 'mt-2');
            submitButton.id = `submit-comment-btn-${commentDiv.id}`;

            commentForm.appendChild(label);
            commentForm.appendChild(textarea);
            commentForm.appendChild(submitButton);

            commentDiv.appendChild(commentForm);
        }
    }
});

//시간
function timeForToday(value) {
    const today = new Date();
    const timeValue = new Date(value);
    const betweenTime = Math.floor((today.getTime() - timeValue.getTime()) / 1000 / 60);

    if (betweenTime < 1) return '방금전';
    if (betweenTime < 60) {
        return `\${betweenTime}분전`;
    }

    const betweenTimeHour = Math.floor(betweenTime / 60);
    if (betweenTimeHour < 24) {
        return `\${betweenTimeHour}시간전`;
    }

    const betweenTimeDay = Math.floor(betweenTime / 60 / 24);
    if (betweenTimeDay < 365) {
        return `\${betweenTimeDay}일전`;
    }

    return `\${Math.floor(betweenTimeDay / 365)}년전`;
}
