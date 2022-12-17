export const dateParser = (date: string): Date => {
  return new Date(date)
}

export const dateToString = (date: Date | undefined): string => {
  if (date) {
    // if date is yesterday, return "yesterday"
    // if date is today, return "today"
    if (isYesterday(date)) {
      return 'yesterday'
    } else if (isToday(date)) {
      return 'today'
    }
    // convert date to mm.dd.yyyy without time and return
    return formatDate(date)
  } else {
    return ''
  }
}

export const isToday = (date: Date): boolean => {
  const today = new Date()
  return date.toDateString() === today.toDateString()
}

export const isYesterday = (date: Date): boolean => {
  const yesterday = new Date()
  yesterday.setDate(yesterday.getDate() - 1)
  return date.toDateString() === yesterday.toDateString()
}

const padTo2Digits = (num: number): string => {
  return num.toString().padStart(2, '0')
}

export const formatDate = (date: Date): string => {
  return [padTo2Digits(date.getDate()), padTo2Digits(date.getMonth() + 1), date.getFullYear()].join('.')
}

export const checkIfStringIsNumber = (str: string): boolean => {
  return !isNaN(Number(str)) && str !== ''
}

export const parseStringLength = (str: string): string => {
  if (str.length > 15) {
    return str.slice(0, 15) + '...'
  }
  return str
}
