export interface ScreenEventsPlugin {
    echo(options: {
        value: string;
    }): Promise<{
        value: string;
    }>;
}