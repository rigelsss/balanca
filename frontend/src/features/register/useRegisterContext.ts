import { useQuery } from "@tanstack/react-query";
import { fetchRegisterContext } from "./registerApi";

export function useRegisterContext() {
  return useQuery({
    queryKey: ["register", "context"],
    queryFn: fetchRegisterContext,
  });
}
