import { useQuery } from "@tanstack/react-query";
import { fetchEntries } from "./historyApi";

export function useEntries() {
  return useQuery({
    queryKey: ["entries"],
    queryFn: fetchEntries,
  });
}
