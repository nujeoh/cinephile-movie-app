import { toast } from 'react-toastify';

const ERROR_TOAST_ID = 'GLOBAL_ERROR';

export function showErrorToast(message) {
  if (toast.isActive(ERROR_TOAST_ID)) {
    toast.update(ERROR_TOAST_ID, {
      render: message,
      type: toast.TYPE.ERROR,
      autoClose: 3000,
    });
  } else {
    toast.error(message, {
      toastId: ERROR_TOAST_ID,
      autoClose: 3000,
    });
  }
}
