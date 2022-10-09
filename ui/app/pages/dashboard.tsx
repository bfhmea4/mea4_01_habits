import { NextPage } from 'next'
import AreaChart from '../components/dashboard/AreaChart'
import Heading from '../components/general/typo/Heading'
import { faker } from '@faker-js/faker'
import Stats from '../components/dashboard/Stats'
import InputField from '../components/general/forms/InputField'
import StyledButton, { StyledButtonType } from '../components/general/buttons/StyledButton'
import { CalculatorIcon } from '@heroicons/react/24/outline'
import { Toast, ToastType } from '../components/alerts/Toast'
import Api from '../config/Api'

const labels = ['January', 'February', 'March', 'April', 'May', 'June', 'July']

export const areaData = {
  labels,
  datasets: [
    {
      fill: true,
      label: 'Dataset 2',
      data: labels.map(() => faker.datatype.number({ min: 0, max: 100 })),
      borderColor: 'black',
      backgroundColor: 'rgba(0, 0, 0, 0.5)',
    },
  ],
}

const callFizzBuzz = async () => {
  const input = document.getElementById('fizzbuzz') as HTMLInputElement

  if (input.value === '') {
    Toast('Please enter a number', ToastType.warning)
    document.getElementById('fizzbuzz')?.focus()
    return
  }

  Api.get('/' + input.value)
    .then(response => {
      Toast(response.data, ToastType.success)
    })
    .catch(error => {
      Toast('Error: ' + error?.response?.data, ToastType.error)
      console.log(error)
    })
}

const Dashboard: NextPage = () => {
  return (
    <div className="">
      <Heading>Dashboard</Heading>

      <div className="mb-2 w-64">
        <InputField name="fizzbuzz" label="Fizzbuzz" required={true} type="number" min={1}></InputField>
        <StyledButton
          name="Calculate"
          type={StyledButtonType.Primary}
          icon={CalculatorIcon}
          iconAnimation={true}
          onClick={callFizzBuzz}
        />
      </div>

      <Stats />
      <AreaChart data={areaData} />
    </div>
  )
}

export default Dashboard
