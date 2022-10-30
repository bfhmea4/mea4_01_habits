export const dateParser = (date: string): Date => {
    return new Date(date);
}

export const dateToString = (date: Date | undefined): string => {
    if (date) {
        // if date is yesterday, return "yesterday"
        // if date is today, return "today"
        if (isYesterday(date)) {
            return "yesterday";
        } else if (isToday(date)) {
            return "today";
        }
        // convert date to mm.dd.yyyy without time and return
        return "";
    } else {
        return '';
    }
}

export const isToday = (date: Date): boolean => {
    const today = new Date();
    return date.toDateString() === today.toDateString();
}

export const isYesterday = (date: Date): boolean => {
    const yesterday = new Date();
    yesterday.setDate(yesterday.getDate() - 1);
    return date.toDateString() === yesterday.toDateString();
}
