import { useQuery } from "@tanstack/react-query";
import { fetchCatalogs } from "./catalogsApi";

export function useCatalogs() {
  return useQuery({
    queryKey: ["catalogs", "admin"],
    queryFn: fetchCatalogs,
  });
}
