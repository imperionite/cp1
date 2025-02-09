import * as yup from "yup";

const loginSchema = yup
  .object({
    identifier: yup.string().required("Username or email is required"),
    password: yup
      .string()
      .min(8, "Password must be at least 8 characters")
      .required("Password is required"),
  })
  .required();


const schema = {
    loginSchema,
}

export default schema