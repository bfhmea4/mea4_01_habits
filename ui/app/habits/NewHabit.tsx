import { PlusIcon } from "@heroicons/react/24/outline";

export const NewHabit = () => {
  const handleOnClick = () => {
    console.log("clicked");
  };

  return (
    <div
      className="habit-card bg-white border-4 border-primary rounded-lg max-w-lg py-2 pr-5 my-4 text-primary shadow-lg select-none cursor-pointer active:hover:scale-105 transition-all duration-200"
      onClick={handleOnClick}
    >
      <div className="flex flex-row justify-between items-center px-4">
        <div className="flex flex-row items-center">
          <PlusIcon className="w-6 h-6" />
          <div className="flex flex-col ml-4">
            <h1 className="text-xl font-bold">New Habit</h1>
            <p className="text-sm">Add a new habit</p>
          </div>
        </div>
      </div>
    </div>
  );
};
