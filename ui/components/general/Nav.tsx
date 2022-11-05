import Image from "next/image";

export const Nav = () => {
  const handleAddJournalEntry = () => {
    console.log("Add journal entry");
  };

  return (
    <div>
      <button
        className="z-10 fixed bottom-6 right-6 rounded-full bg-white border-4 border-primary p-3 cursor-pointer active:hover:scale-105 transition-all duration-150 ease-in-out"
        onClick={handleAddJournalEntry}
      >
        <Image
          src="/images/icons/notebook.svg"
          alt="Personal Goals"
          width={40}
          height={35}
          unoptimized
        />
      </button>
    </div>
  );
};
