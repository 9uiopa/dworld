const upvoteBtn = document.getElementById('upvote-btn');
const downvoteBtn = document.getElementById('downvote-btn');

function vote(voteType) {
    return function() {
        let articleId = document.getElementById('article-id').value;
        fetch(`/api/articles/${articleId}/vote`, {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                voteType: voteType,
            })
        })
            .then(response => {
                switch (response.status){
                    case 201:
                        // 성공적인 응답 처리
                        alert('추천되었습니다.')
                        location.replace(`/articles/${articleId}`);
                        return;
                    case 401:
                        // 인증되지 않은 사용자
                        alert('로그인 후에만 추천 가능합니다.');
                        return;
                    case 403:
                        // 이미 추천
                        alert('이미 추천/비추천 하셨습니다.')
                        return;
                    case 500:
                        //서버 에러
                        alert('서버 에러 발생')
                        return;
                    default:
                        alert('알 수 없는 에러')
                        return;
                }
            })
            .catch(error => {
                // 네트워크 오류 처리
                console.error('Error during fetch:', error);
                alert('네트워크 에러');
            });
    }
}

// 이벤트 리스너 설정
upvoteBtn.addEventListener('click', vote('UPVOTE'));
downvoteBtn.addEventListener('click', vote('DOWNVOTE'));
