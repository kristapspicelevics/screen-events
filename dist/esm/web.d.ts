import { WebPlugin } from '@capacitor/core';
import type { ScreenEventsPlugin } from './definitions';
export declare class ScreenEventsWeb extends WebPlugin implements ScreenEventsPlugin {
    echo(options: {
        value: string;
    }): Promise<{
        value: string;
    }>;
}
