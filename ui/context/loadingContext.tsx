import { createContext, ReactNode, useContext, useEffect, useState } from "react";

export const LoadingContext = createContext({});

export const useLoadingContext = () => {
    return useContext(LoadingContext);
};

type Props = {
    children: ReactNode;
};

export const LoadingContextProvider = ({ children }: Props) => {
    const [loading, setLoading] = useState(true);
    const [componentLoading, setComponentLoading] = useState(false);
    const [error, setError] = useState(null);
    const [reload, setReload] = useState(false);

    const contextValue = {
        loading,
        componentLoading,
        error,
        reload,
        setLoading,
        setComponentLoading,
        setError,
        setReload,
    };

    return (
        <LoadingContext.Provider value={contextValue}>
            {children}
        </LoadingContext.Provider>
    )
}
