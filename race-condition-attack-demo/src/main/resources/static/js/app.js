const reviewers = ['Joe', 'Tom', 'Kevin'];

document.addEventListener('DOMContentLoaded', function () {
    loadTeachers();

    async function loadTeachers() {
        const response = await fetch('/api/v1/teachers');
        const teachers = await response.json();

        const container = document.getElementById('teachers');
        container.innerHTML = '';

        teachers.forEach((teacher, index) => {
            const avgRanking = teacher.averageRanking != null ? teacher.averageRanking.toFixed(2) : 'N/A';

            const reviews = teacher.rankings.map((review, idx) => {
                const reviewer = reviewers[(index + idx) % reviewers.length];
                const date = new Date(review.dateAdded).toLocaleDateString();
                return `<li class="list-group-item">${reviewer} (${date}) — Score: ${review.score} — ${review.comment}</li>`;
            }).join('');

            const html = `
                <div class="card mb-4">
                    <div class="card-body">
                        <h5 class="card-title">${teacher.name}</h5>
                        <p class="card-text">Average Ranking: <strong>${avgRanking}</strong></p>
                        <ul class="list-group mb-3">${reviews}</ul>
                        <form onsubmit="return submitReview(event, '${teacher.id}')">
                            <input type="hidden" name="reviewerId" value="c884000b-2333-4960-8be1-8bd9c11f1da9"/>
                            <input type="hidden" name="teacherId" value="${teacher.id}"/>
                            <div class="mb-2">
                                <label class="form-label">Score</label>
                                <select name="score" class="form-select" required>
                                    <option value="" disabled selected>Select score</option>
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="3">3</option>
                                    <option value="4">4</option>
                                    <option value="5">5</option>
                                </select>
                            </div>
                            <div class="mb-2">
                                <label class="form-label">Comment</label>
                                <input type="text" name="comment" class="form-control" required>
                            </div>
                            <button type="submit" class="btn btn-primary">Add Review</button>
                        </form>
                    </div>
                </div>
            `;
            container.insertAdjacentHTML('beforeend', html);
        });
    }

    window.submitReview = async function (event, teacherId) {
        event.preventDefault();
        const form = event.target;
        const formData = new FormData(form);

        const score = formData.get('score');
        if (!score) {
            alert('Please select a score.');
            return;
        }

        const payload = {
            reviewerId: formData.get('reviewerId'),
            teacherId: formData.get('teacherId'),
            score: parseInt(score),
            comment: formData.get('comment'),
            date: new Date().toISOString()
        };

        const response = await fetch('/api/v1/teachers', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(payload)
        });

        if (response.status === 409) {
            showConflictToast();
            return;
        }

        loadTeachers();
    };
});

function showConflictToast() {
    const toastEl = document.getElementById('conflictToast');
    const toast = new bootstrap.Toast(toastEl);
    toast.show();
}
