function initToast(successMsg, errorMsg) {
    const Toast = Swal.mixin({
        toast: true,
        position: 'top-end',
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
        didOpen: (toast) => {
            toast.addEventListener('mouseenter', Swal.stopTimer)
            toast.addEventListener('mouseleave', Swal.resumeTimer)
        }
    });

    if (successMsg && successMsg.trim() !== "") {
        Toast.fire({
            icon: 'success',
            title: successMsg
        });
    }

    if (errorMsg && errorMsg.trim() !== "") {
        Toast.fire({
            icon: 'error',
            title: errorMsg
        });
    }
}
