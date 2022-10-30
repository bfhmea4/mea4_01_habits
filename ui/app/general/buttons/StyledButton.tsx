import { SVGProps } from "react";
import { classNames } from "../../../lib/design";

export interface StyledButtonProps {
    name: string;
    icon?: (props: SVGProps<SVGSVGElement>) => JSX.Element;
    iconAnimation?: boolean;
    type?: StyledButtonType;
    onClick: () => void;
    className?: string;
    small?: boolean | undefined;
}

export enum StyledButtonType {
    Primary = 'primary',
    Secondary = 'secondary',
    Danger = 'danger'
}

const StyledButton = (props: StyledButtonProps) => {

    const hoverAnimationClasses = classNames(
        props.type === StyledButtonType.Primary ? "active:hover:bg-primary-dark sm:hover:bg-primary-dark" : "",
        props.type === StyledButtonType.Secondary ? "active:hover:bg-primary-darksm:hover:bg-primary-dark" : "",
        props.type === StyledButtonType.Danger ? "active:hover:bg-red-700 sm:hover:bg-red-700" : "",
        "transition-all duration-150 ease-in-out"
    )
    const defaultStyleClasses = classNames(
        props.type === StyledButtonType.Primary ? "bg-primary text-white" : "",
        props.type === StyledButtonType.Secondary ? "border-2 bg-white border-primary text-gray-700" : "",
        props.type === StyledButtonType.Danger ? "bg-red-600 text-white" : "",
        props.small ? "h-9 text-xs py-2" : "h-14 py-4 text-sm px-11",
        "rounded-lg px-4 overflow-hidden group font-medium",
    )

    return (
        <button
            className={classNames(
                defaultStyleClasses,
                hoverAnimationClasses,
                props.className ? props.className : ""
            )}
            onClick={props.onClick}
        >
            {props.icon && !props.iconAnimation &&
                <props.icon className={classNames(
                    props.small ? "h-4 w-4 inline mr-2" : "h-5 w-5 inline mr-2"
                )}
                />}
            {props.name}
            {props.icon && props.iconAnimation && <props.icon className={classNames(
                props.small ? "h-4 w-4" : "h-5 w-5",
                "inline ml-2 group-hover:translate-x-1 transition-all ease-in-out duration-150"
            )} />}

        </button>
    )

}

export default StyledButton
