import { useQuery } from "@tanstack/react-query";
import { fetchBatches } from "./batchesApi";

export function useBatches() {
  return useQuery({
    queryKey: ["batches"],
    queryFn: fetchBatches,
  });
}
