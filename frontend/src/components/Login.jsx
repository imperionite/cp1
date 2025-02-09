import { useForm } from 'react-hook-form';
import { useMutation } from '@tanstack/react-query';
import { useAtom } from 'jotai';
import { accessTokenAtom, refreshTokenAtom } from '../atoms/store';
import * as yup from 'yup';
import { yupResolver } from '@hookform/resolvers/yup';
import httpService from '../services/http'

const schema = yup.object({
  identifier: yup.string().required('Identifier is required'),
  password: yup.string().min(6, 'Password must be at least 6 characters').required('Password is required'),
});

const Login = () => {
  const { register, handleSubmit, formState: { errors } } = useForm({
    resolver: yupResolver(schema),
  });

  const [accessToken, setAccessToken] = useAtom(accessTokenAtom);
  const [refreshToken, setRefreshToken] = useAtom(refreshTokenAtom);

  const mutation = useMutation({
    mutationFn: httpService.loginRequest,
    onSuccess: (data) => {
      setAccessToken(data.accessToken);
      setRefreshToken(data.refreshToken);
      console.log('Login successful, tokens set');
      // Redirect to a new page or perform any other action after successful login
    },
    onError: (error) => {
      console.error('Login failed: ', error);
      alert('Login failed, please check your credentials.');
    }
  });

  const onSubmit = (data) => {
    mutation.mutate({ identifier: data.identifier, password: data.password });
  };

  console.log(accessToken)
  console.log(refreshToken)

  return (
    <div className="container">
      <h2>Login</h2>
      <form onSubmit={handleSubmit(onSubmit)}>
        <div className="form-group">
          <label htmlFor="identifier">Username or Email</label>
          <input
            type="text"
            id="identifier"
            {...register('identifier')}
            placeholder="Enter your username or email"
          />
          {errors.identifier && <span>{errors.identifier.message}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="password">Password</label>
          <input
            type="password"
            id="password"
            {...register('password')}
            placeholder="Enter your password"
          />
          {errors.password && <span>{errors.password.message}</span>}
        </div>

        <button type="submit" disabled={mutation.isLoading}>
          {mutation.isLoading ? 'Logging in...' : 'Login'}
        </button>
      </form>

      {mutation.isError && <p className="error">Something went wrong. Please try again.</p>}
    </div>
  );
};

export default Login